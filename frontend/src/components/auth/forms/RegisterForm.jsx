import React from 'react';
import {MDBBtn, MDBInput} from "mdb-react-ui-kit";
import {Providers, REGISTER_URL, RegisterFlow} from "../../../tools/consts";
import {validateEmail} from "../../../tools/functions";
import {useDispatch} from "react-redux";
import {setError, setErrorMessage} from "../../../redux/actions";

const RegisterForm = ({setProvider, setFlow, userData,
                          setEmail, setPassword, setName, setSurname, setBirthDate}) => {

    const dispatch = useDispatch();
    const isPhone = window.innerWidth < 768;

    const nextHandler = async () => {
        if (userData.email.length === 0 || userData.password.length === 0
            || userData.name.length === 0 || userData.surname.length === 0 || userData.birthDate.length === 0) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Пожалуйста, заполните все поля формы"));
            return;
        }
        if (userData.password.length < 6) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Пароль слишком короткий. Пожалуйста, придумайте другой пароль."));
            return;
        }
        if (userData.name.length < 4) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Имя слишком короткое. Пожалуйста, введите свое полное имя."));
            return;
        }
        if (userData.surname.length < 4) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Фамилия слишком короткая. Пожалуйста, введите свою фамилию."));
            return;
        }
        const validEmail = validateEmail(userData.email);
        if (!validEmail) {
            dispatch(setError(true));
            dispatch(setErrorMessage("Электронная почта не валидна. Пожалуйста, попробуйте снова."));
            return;
        }
        delete userData.desiredRole;

        fetch(REGISTER_URL, {
            method: "POST",
            body: JSON.stringify(userData),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => {
            if (response.ok) {
                return response.json();
            } else {
                return {success: false, description: "Возникла проблема. Пожалуйста, попробуйте снова позже."}
            }
        }).then(data => {
            if (data.success) {
                setFlow(RegisterFlow.EmailVerification);
            } else {
                dispatch(setError(true));
                dispatch(setErrorMessage(data.description));
            }
        });
    };

    return (
        <div className="container pt-3 h-100">
            <div className="row d-flex justify-content-center align-items-center h-100">
                <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div className="card bg-light text-dark" style={{borderRadius: "1rem"}}>
                        <div className="card-body p-5 text-center pb-4">
                            <div className="my-md-4 pb-2">
                                <h2 className="fw-bold mb-2 text-uppercase">Регистрация</h2>
                                <p className="text-dark-50 mb-4">Пожалуйста, заполните форму!</p>

                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setEmail(event.target.value)} type="email"
                                              id="typeEmailX" label="Электронная почта"
                                              className="form-control form-control-lg"/>
                                </div>
                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setPassword(event.target.value)}
                                              type="password" id="typePasswordX" label="Пароль"
                                              className="form-control form-control-lg"/>
                                </div>
                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setName(event.target.value)} type="text"
                                              id="typeFirstnameX" label="Имя"
                                              className="form-control form-control-lg"/>
                                </div>
                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setSurname(event.target.value)} type="text"
                                              id="typeLastnameX" label="Фамилия"
                                              className="form-control form-control-lg"/>
                                </div>
                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setBirthDate(event.target.value)} type="date"
                                              id="typeBirthDateX" label="Дата рождения"
                                              className="form-control form-control-lg"/>
                                </div>

                                <MDBBtn outline className="btn btn-outline-dark btn-lg px-5 mt-3"
                                        type="submit" onClick={nextHandler}>Далее</MDBBtn>
                            </div>
                            <div className={`${isPhone && "mt-3"}`}>
                                <p className="mb-0">Вы тренер или администратор? <a href="#!"
                                                                               onClick={(event) => {
                                                                                   event.preventDefault();
                                                                                   setFlow(RegisterFlow.Application);
                                                                               }}
                                                                               className="text-dark-50">Перейти</a>
                                </p>
                                <p className="mb-0">Хотите вернуться назад? <a href="#!"
                                                                               onClick={(event) => {
                                                                                   event.preventDefault();
                                                                                   setProvider(Providers.LoginProvider);
                                                                               }}
                                                                               className="text-dark-50">Назад</a>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RegisterForm;
