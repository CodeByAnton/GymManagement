import React, {useState} from 'react';
import {backgroundStyle, Providers} from "../../../tools/consts";
import LoginProvider from "./LoginProvider";
import RegisterProvider from "./RegisterProvider";
import ChangePasswordProvider from "./ChangePasswordProvider";
import Logout from "../forms/Logout";
import {useDispatch, useSelector} from "react-redux";
import {selectAuthenticated} from "../../../redux/selectors";
import { setAuthenticated } from '../../../redux/actions';

const AuthenticationProvider = ({isAdmin}) => {
    const [provider, setProvider] = useState(Providers.LoginProvider);

    const dispatch = useDispatch();
    const authenticated = useSelector(selectAuthenticated);

    return (
        <span style={backgroundStyle}>
            {!authenticated
                && provider === Providers.LoginProvider
                && <LoginProvider setProvider={setProvider}
                                  isAdmin={isAdmin}
                />}
            {!authenticated
                && provider === Providers.RegisterProvider
                && <RegisterProvider setProvider={setProvider}
                />}
            {!authenticated
                && provider === Providers.ChangePasswordProvider
                &&
                <ChangePasswordProvider
                    backToLogin={() => {
                        dispatch(setAuthenticated(false));
                        setProvider(Providers.LoginProvider);
                    }}/>}
            {authenticated && <div
                className="vh-100 container-fluid d-flex gradient-custom align-items-center justify-content-center">
                <Logout backToLogin={() => {
                    dispatch(setAuthenticated(false));
                    setProvider(Providers.LoginProvider);
                }}/></div>}
        </span>
    );
};

export default AuthenticationProvider;