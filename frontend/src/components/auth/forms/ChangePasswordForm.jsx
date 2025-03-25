import React from 'react';
import {MDBInput, MDBBtn} from 'mdb-react-ui-kit';
import {RegisterFlow as ChangePasswordFlow, SEND_OTP_URL} from "../../../tools/consts";
import {validateEmail} from "../../../tools/functions";
import {useDispatch} from "react-redux";
import {setError, setErrorMessage} from "../../../redux/actions";

const ChangePasswordForm = ({userData, backToLogin, setFlow, setEmail, setNewPassword}) => {

    const dispatch = useDispatch();

    const nextHandler = async () => {

        const validEmail = validateEmail(userData.email);
        if (!validEmail) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Некорректная электронная почта. Пожалуйста попробуйте позже."));
            return;
        }

        fetch(SEND_OTP_URL + userData.email, {
            method: 'POST',
        }).then(response => response.json())
            .then(data => {
                if (data.success) {
                    setFlow(ChangePasswordFlow.EmailVerification);
                } else {
                    dispatch(setError(true));
                    dispatch(setErrorMessage("Некорректная электронная почта. Пожалуйста попробуйте позже."));
                }
            })
    }

    const isPhone = window.innerWidth < 768;

    return (
            <div className="container">
                <div className="row d-flex justify-content-center align-items-center h-100">
                    <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                        <div className="card bg-light text-dark" style={{borderRadius: "1rem"}}>
                            <div className="card-body p-5 pb-4 text-center">

                                <div className="mb-md-3 mt-md-4 pb-3">

                                    <h2 className="fw-bold mb-2 text-uppercase">Поменять пароль</h2>
                                    <p className={`text-dark-50 ${isPhone ? "mb-4": "mb-5"}`}>Введите электронную почту и новый пароль!</p>

                                    <div className="form-outline form-dark mb-4">
                                        <MDBInput type="email"
                                                  onChange={(event) => setEmail(event.target.value)}
                                                  id="typeEmailX" label="Электронная почта"
                                                  className="form-control form-control-lg"/>
                                    </div>
                                    <div className={`form-outline form-dark ${isPhone ? "mb-2" : "mb-4"}`}>
                                        <MDBInput type="password"
                                                  onChange={(event) => setNewPassword(event.target.value)}
                                                  id="typePasswordX" label="Новый пароль"
                                                  className="form-control form-control-lg"/>
                                    </div>
                                    <div className="container">
                                        <div className={`row mx-2 ${isPhone ? 'flex-column' : ''}`}>
                                            <MDBBtn
                                                outline
                                                onClick={backToLogin}
                                                className={`col btn btn-outline-danger btn-lg px-5 ${isPhone ? 'col-12 mt-4' : 'col-5 mt-4'}`}
                                                type="submit"
                                            >
                                                Отмена
                                            </MDBBtn>

                                            <MDBBtn
                                                outline
                                                onClick={nextHandler}
                                                className={`col btn btn-outline-dark btn-lg px-5 ${isPhone ? 'col-12 mt-3' : 'offset-2 mt-4'}`}
                                                type="submit"
                                            >
                                                Далее
                                            </MDBBtn>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    );
};

export default ChangePasswordForm;