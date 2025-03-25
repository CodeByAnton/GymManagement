import {MDBInput, MDBBtn} from 'mdb-react-ui-kit';
import {LOGIN_URL, LoginFlow, Providers} from "../../../tools/consts";
import {validateEmail} from "../../../tools/functions";
import {setError, setErrorMessage} from "../../../redux/actions";
import {useDispatch} from "react-redux";

const LoginForm = ({userData, setEmail, setPassword, setProvider, setFlow, isAdmin}) => {
    const dispatch = useDispatch();

    const loginHandler = () => {
        const validEmail = validateEmail(userData.email);
        if (!validEmail) {
            dispatch(setError(true))
            dispatch(setErrorMessage("Некорректный адрес электронной почты. Пожалуйста, попробуйте снова."));
            return;
        }

        fetch(LOGIN_URL, {
            method: 'POST',
            body: JSON.stringify(userData),
            headers: {
                "Content-Type": "application/json"
            }
        }).then(response => response.json())
            .then(data => {
                if (data.success) {
                    setFlow(LoginFlow.EmailVerification);
                } else {
                    dispatch(setError(true))
                    dispatch(setErrorMessage('Неправильная электронная почта или пароль. Пожалуйста попробуйте снова.'));
                }
            })
    }

    const isPhone = window.innerWidth < 768;

    const adminClasses = "mb-md-1 mt-md-4" + isPhone ? "pb-4" : "pb-3";
    const userClasses = "mb-md-5 mt-md-4" + isPhone ? "pb-4" : "pb-3";

    const loginButtonAdmin = {marginTop: '8%'};

    return (
        <div className="container py-4 h-100">
            <div className="row d-flex justify-content-center align-items-center h-100">
                <div className="col-12 col-md-8 col-lg-6 col-xl-5">
                    <div className="card bg-light text-dark" style={{borderRadius: "1rem", width: '100%'}}>
                        <div className={`card-body text-center p-5 pb-4`}>

                            <div className={isAdmin ? adminClasses : userClasses}>

                                <h2 className="fw-bold mb-2 text-uppercase">Войти</h2>
                                <p className={`text-dark-50 ${isPhone ? "mb-4" : "mb-5"}`}>Пожалуйста, введите свою
                                    электронную почту и пароль!</p>

                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setEmail(event.target.value)} type="email"
                                              id="typeEmailX" label="Электронная почта"
                                              className="form-control form-control-lg"/>
                                </div>
                                <div className="form-outline form-dark mb-4">
                                    <MDBInput onChange={(event) => setPassword(event.target.value)} type="password"
                                              id="typePasswordX" label="Пароль"
                                              className="form-control form-control-lg"/>
                                </div>
                                {
                                    !isAdmin &&
                                    <p className={`small ${isPhone ? "mb-4" : "mb-5"} pb-lg-2`}><a
                                        className="text-dark-50 fs-6" href="#!"
                                        onClick={(event) => {
                                            event.preventDefault();
                                            setProvider(Providers.ChangePasswordProvider);
                                        }}>Забыли пароль?</a></p>
                                }

                                <MDBBtn outline onClick={loginHandler}
                                        className={`btn btn-outline-dark btn-lg px-5 ${!isPhone && "mb-3"}`}
                                        style={isAdmin ? loginButtonAdmin : {}}
                                        type="submit">Войти</MDBBtn>
                            </div>
                            {
                                !isAdmin &&
                                <div>
                                    <p className={`mb-0 ${isPhone && "fs-6"}`}>Еще нет аккаунта? <a href="#!"
                                                                                                    onClick={(event) => {
                                                                                                        event.preventDefault();
                                                                                                        setProvider(Providers.RegisterProvider);
                                                                                                    }}
                                                                                                    className="text-dark-50 fs-6">Регистрация</a>
                                    </p>
                                </div>
                            }

                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginForm;