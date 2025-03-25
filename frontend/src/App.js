import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { setAuthenticated, setRole, setAccessToken, setRefreshToken, setSuccess, setError } from './redux/actions.js';
import { updateTokensOrLogout } from './tools/functions';
import UserPage from "./components/app/pages/UserPage";
import ErrorMessage from "./tools/ErrorMessage";
import SuccessMessage from "./tools/SuccessMessage";
import {selectError, selectErrorMessage, selectSuccess, selectSuccessMessage} from './redux/selectors';

function App() {
    const dispatch = useDispatch();
    const error = useSelector(selectError);
    const errorMessage = useSelector(selectErrorMessage);
    const success = useSelector(selectSuccess);
    const successMessage = useSelector(selectSuccessMessage);

    useEffect(() => {
        updateTokensOrLogout(
            (newAccessToken) => dispatch(setAccessToken(newAccessToken)),
            (newRefreshToken) => dispatch(setRefreshToken(newRefreshToken)),
            (isAuthenticated) => dispatch(setAuthenticated(isAuthenticated)),
            (newRole) => dispatch(setRole(newRole))
        ).then();
    }, [dispatch]);

    return (
        <>
            {success && <SuccessMessage message={successMessage} onClose={() => dispatch(setSuccess(false))} />}
            {error && <ErrorMessage message={errorMessage} onClose={() => dispatch(setError(false))} />}
            <UserPage />
        </>
    );
}

export default App;