import React, {useEffect, useState, useRef} from 'react';
import ErrorMessage from "../../../tools/ErrorMessage";
import SuccessMessage from "../../../tools/SuccessMessage";
import {MDBBtn} from "mdb-react-ui-kit";
import {asyncPostRequest} from "../../../tools/functions";
import {SEND_OTP_URL} from "../../../tools/consts";
import {useDispatch} from "react-redux";
import {
    setAccessToken,
    setAuthenticated,
    setError,
    setErrorMessage,
    setRefreshToken,
    setRole, setSuccess, setSuccessMessage
} from "../../../redux/actions";

const EmailVerificationForm = ({URL, cancelHandler, userData}) => {
    const [otp, setOtp] = useState(new Array(6).fill(""));
    const inputRefs = useRef([]);
    const digits = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9];

    const [resendTimer, setResendTimer] = useState(60);
    const [canResend, setCanResend] = useState(false);

    const isPhone = window.innerWidth <= 768;

    const dispatch = useDispatch();

    useEffect(() => {
        if (inputRefs.current[0]) {
            inputRefs.current[0].focus();
        }
    }, []);

    useEffect(() => {
        if (resendTimer > 0) {
            const timer = setTimeout(() => {
                setResendTimer(resendTimer - 1);
            }, 1000);
            return () => clearTimeout(timer);
        } else {
            setCanResend(true);
        }
    }, [resendTimer]);

    function validateOTP() {
        for (let char of otp) {
            if (char === "" || !(char in digits)) {
                return false;
            }
        }
        return true;
    }

    function otpToString() {
        let res = "";
        for (let char of otp) {
            res += char;
        }
        return res;
    }

    const submitHandler = () => {
        if (!validateOTP()) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Код не введен не полностью"));
            return;
        }
        asyncPostRequest({...userData, otp: otpToString()}, URL).then(response => {
            if (response.status !== 200) {
                dispatch(setError(true));
                dispatch(setErrorMessage("Не верный код. Пожалуйста попробуйте снова"));
                setOtp(new Array(6).fill(""));
                return response.json();
            }
            return response.json();
        }).then(data => {
            console.log(data.role);
            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
            localStorage.setItem("role", data.role);
            dispatch(setAccessToken(data.accessToken))
            dispatch(setRefreshToken(data.refreshToken))
            dispatch(setAuthenticated(true))
            dispatch(setRole(data.role))
        })
    };

    const handleKeyDown = (element, index, event) => {
        if (event.key in digits) {
            setOtp((prevState) => {
                let newOtp = [...prevState];
                newOtp[index] = event.key;
                return newOtp;
            })
            if (index < 5) {
                element.nextSibling.focus();
            }
        } else if (event.key === "Backspace") {
            if (element.value === "" && index !== 0) {
                element.previousSibling.focus();
            } else {
                setOtp((prevState) => {
                    let newOtp = [...prevState];
                    newOtp[index] = "";
                    return newOtp;
                })
            }
        }
    };

    const resendOtpHandler = (event) => {
        event.preventDefault();
        fetch(SEND_OTP_URL  + userData.email, {
            method: 'POST', headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            console.log(response.status)
            if (response.status !== 200) {
                dispatch(setError(true));
                dispatch(setErrorMessage("Не удалось повторо отправить код подтверждения. Попробуйте позже."));
            } else {
                dispatch(setSuccess(true));
                dispatch(setSuccessMessage('Код подтверждения был отправлен повторно.'));
                setResendTimer(60);
                setCanResend(false);
            }
        });
    }

    return (
        <>
            <div className="container">
                <div
                    className={`container d-flex justify-content-center align-items-center w-50 px-4 text-center bg-light text-dark rounded-top-4 h2 mb-0 ${isPhone ? 'w-100 py-4' : 'py-5'}`}>
                    <div className="row rounded-3 ms-2 mx-0">
                        <div className="col text-center px-0">
                            Введите код, отправленный на вашу электронную почту
                        </div>
                    </div>
                </div>
                <div
                    className={`container-sm px-3 pt-2 text-center bg-light text-dark rounded-bottom-4 h2 w-50 h-100 pb-5 ${isPhone ? 'w-100' : ''}`}>
                    <div className="row rounded-3 m-0">
                        <div className="col text-center">
                            <div className="form-group d-flex justify-content-center">
                                {otp.map((data, index) => (
                                    <input
                                        className="otp-input form-control mx-1 text-center fs-2"
                                        type="text"
                                        name="otp"
                                        maxLength="1"
                                        key={index}
                                        value={data}
                                        onChange={() => {
                                        }}
                                        onKeyDown={e => handleKeyDown(e.target, index, e)}
                                        onFocus={e => e.target.select()}
                                        ref={el => inputRefs.current[index] = el}
                                    />
                                ))}
                            </div>
                        </div>
                    </div>
                    <div className="mt-4">
                        {
                            canResend ?
                                <a className="text-dark-50 fs-5" href="" onClick={resendOtpHandler}>
                                    Не пришло сообщение? Отправить новый код.</a>
                                :
                                <a className="text-dark-50 fs-5" href=""
                                   style={{pointerEvents: 'none'}}>
                                    Не пришло сообщение? Отправить новый код через {resendTimer} сек.
                                </a>
                        }

                    </div>
                    <div className={`row ${isPhone ? 'flex-column' : ''}`}>
                        <MDBBtn outline
                                className={`col btn btn-outline-danger btn-lg px-5 ${isPhone ? 'offset-1 col-10 mt-4' : 'offset-1 col-4 mt-5'}`}
                                type="submit" onClick={cancelHandler}>Отмена</MDBBtn>
                        <MDBBtn outline
                                className={`col btn btn-outline-dark btn-lg px-5  ${isPhone ? 'offset-1 col-10 mt-4' : 'offset-2 col-4 mt-5'}`}
                                type="submit" onClick={submitHandler}>Подтвердить</MDBBtn>
                    </div>
                </div>
            </div>
        </>
    );
};

export default EmailVerificationForm;