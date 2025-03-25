import React from 'react';
import {MDBBtn} from "mdb-react-ui-kit";
import {LOGOUT_URL} from "../../../tools/consts";
import  "../../../css/main.css"
import {useDispatch} from "react-redux";
import {
    setAccessToken, setAuthenticated, setError, setErrorMessage,
    setRefreshToken, setRole, setSuccess, setSuccessMessage
} from "../../../redux/actions";

const Logout = () => {

    const dispatch = useDispatch();

    const logoutHandler = () => {
        let refreshToken = localStorage.getItem("refreshToken");
        if(refreshToken === null || refreshToken === undefined){
            dispatch(setError(true));
            dispatch(setErrorMessage('Вы не вошли в аккаунт'));
            return;
        }
        fetch(LOGOUT_URL, {
            method: "POST",
            body: JSON.stringify({
                "refreshToken": refreshToken
            }),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => response.json())
            .then(data => {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Вы успешно вышли из аккаунта.'))
            }).finally(() => {
            localStorage.removeItem("refreshToken");
            localStorage.removeItem("accessToken");
            localStorage.removeItem("role");
            dispatch(setAccessToken(''));
            dispatch(setRefreshToken(''));
            dispatch(setRole(''));
            dispatch(setAuthenticated(false));
        })
    }

    return (
        <>
            <MDBBtn outline onClick={logoutHandler}
                    className="btn btn-outline-dark btn-lg px-5"
                    type="submit">Выйти</MDBBtn>
        </>
    );
};

export default Logout;