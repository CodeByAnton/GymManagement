package com.annton.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.annton.api.data.enums.Role;
import com.annton.api.data.enums.TokenType;
import com.annton.api.dto.AccessTokenDTO;
import com.annton.api.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final Algorithm algorithm;

    public JwtService(@Value("jwt.secret") String secret){
        this.algorithm = Algorithm.HMAC256(secret);
    }


    public String createJwt(String username, TokenType tokenType, Role role){
        return JWT.create()
                .withIssuer("itmo-cw-backend")
                .withSubject(username)
                .withClaim("role", role.name())
                .withClaim("type", tokenType.name())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenType.getTimeAliveMillis()))
                .sign(algorithm);
    }

    public String createAccessToken(String username, Role role){
        return createJwt(username, TokenType.ACCESS, role);
    }
    public String createRefreshToken(String username, Role role){
        return createJwt(username, TokenType.REFRESH, role);
    }

    public AccessTokenDTO createAccessTokenDTO(String username, Role role){
        return new AccessTokenDTO(createAccessToken(username, role));
    }

    public AuthenticationResponse createAuthenticationResponse(String email, Role role){
        return AuthenticationResponse.builder()
                .accessToken(createAccessToken(email, role))
                .refreshToken(createRefreshToken(email, role))
                .role(role.name())
                .build();
    }

    public DecodedJWT decodeAndValidateToken(String jwt) throws JWTVerificationException {
        return JWT.require(algorithm)
                .withIssuer("itmo-cw-backend")
                .build()
                .verify(jwt);
    }

    public String getEmailFromToken(String jwt) throws JWTVerificationException{
        return decodeAndValidateToken(jwt).getSubject();
    }

    public String getEmailFromToken(DecodedJWT decodedJWT) throws JWTVerificationException{
        return decodedJWT.getSubject();
    }

    public TokenType getTypeFromToken(String jwt) throws JWTVerificationException{
        return TokenType.valueOf(decodeAndValidateToken(jwt).getClaim("type").asString());
    }

    public TokenType getTypeFromToken(DecodedJWT decodedJWT) throws JWTVerificationException{
        return TokenType.valueOf(decodedJWT.getClaim("type").asString());
    }

    public Role getRoleFromToken(String jwt) throws JWTVerificationException{
        return Role.valueOf(decodeAndValidateToken(jwt).getClaim("role").asString());
    }

    public Role getRoleFromToken(DecodedJWT decodedJWT) throws JWTVerificationException{
        return Role.valueOf(decodedJWT.getClaim("role").asString());
    }

}
