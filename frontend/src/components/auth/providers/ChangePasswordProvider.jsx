import React, {useState} from 'react';
import ChangePasswordForm from "../forms/ChangePasswordForm";
import {Providers, RESET_PASSWORD_URL, ResetPasswordFlow} from "../../../tools/consts";
import EmailVerificationForm from "../forms/EmailVerificationForm";

const ChangePasswordProvider = ({backToLogin, setProvider}) => {

    const [userData, setUserData] = useState({
        email: '',
        newPassword: '',
    });

    const setNewPassword = (newPassword) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                newPassword: newPassword
            };
        })
    }

    const setEmail = (newEmail) => {
        setUserData((prevState) => {
            return {
                ...prevState,
                email: newEmail
            };
        })
    }


    const [flow, setFlow] = useState(ResetPasswordFlow.ResetPasswordForm)


    return (
        <>
            {flow === ResetPasswordFlow.ResetPasswordForm
                && <ChangePasswordForm userData={userData} setEmail={setEmail} setNewPassword={setNewPassword}
                                       backToLogin={backToLogin}
                                       setFlow={setFlow}/>}
            {flow === ResetPasswordFlow.EmailVerification
                && <EmailVerificationForm
                    URL={RESET_PASSWORD_URL}
                    cancelHandler={() => setProvider(Providers.LoginProvider)}
                    userData={userData}/>}
        </>
    );
};

export default ChangePasswordProvider;