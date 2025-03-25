import React, {useState} from 'react';
import RegisterForm from "../forms/RegisterForm";
import {RegisterFlow, VERIFY_OTP_URL} from "../../../tools/consts";
import EmailVerificationForm from "../forms/EmailVerificationForm";
import ApplicationForm from "../forms/ApplicationForm";

const RegisterProvider = ({setProvider}) => {
    const [flow, setFlow] = useState(RegisterFlow.RegisterForm);
    const [userData, setUserData] = useState({
        email: '',
        password: '',
        name: '',
        surname: '',
        birthDate: '',
        desiredRole: ''
    });

    const setEmail = (newEmail) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                email: newEmail
            }
        })
    }

    const setPassword = (newPassword) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                password: newPassword
            }
        })
    }

    const setName = (newName) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                name: newName
            }
        })
    }

    const setSurname = (newSurname) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                surname: newSurname
            }
        })
    }

    const setBirthDate = (newBirthDate) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                birthDate: newBirthDate
            }
        })
    }
    const setDesiredRole = (newDesiredRole) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                desiredRole: newDesiredRole
            }
        })
    }

    return (
        <>
            {flow === RegisterFlow.RegisterForm
                &&
                <RegisterForm setProvider={setProvider} userData={userData} setFlow={setFlow} setEmail={setEmail}
                              setPassword={setPassword}
                              setName={setName} setSurname={setSurname} setBirthDate={setBirthDate}/>}
            {flow === RegisterFlow.EmailVerification
                && <EmailVerificationForm URL={VERIFY_OTP_URL} userData={{email: userData.email}}
                                          cancelHandler={() => setFlow(RegisterFlow.RegisterForm)}/>}
            {flow === RegisterFlow.Application
                && <ApplicationForm userData={userData} setFlow={setFlow} setEmail={setEmail}
                                 setPassword={setPassword} setDesiredRole={setDesiredRole}
                                 setName={setName} setSurname={setSurname} setBirthDate={setBirthDate}/>}
        </>
    );
};

export default RegisterProvider;