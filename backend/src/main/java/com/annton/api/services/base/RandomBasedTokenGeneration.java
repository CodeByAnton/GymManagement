package com.annton.api.services.base;

import java.util.Random;

public abstract class RandomBasedTokenGeneration {

    private final static Random rand = new Random();

    public String generateToken(int symbolsCount) {
        StringBuilder otpBuilder = new StringBuilder();
        for(int i = 0; i < symbolsCount; i++){
            otpBuilder.append(rand.nextInt(10));
        }
        return otpBuilder.toString();
    }

}
