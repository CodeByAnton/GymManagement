export const SET_AUTHENTICATED = 'SET_AUTHENTICATED';
export const SET_ROLE = 'SET_ROLE';
export const SET_ERROR = 'SET_ERROR';
export const SET_SUCCESS = 'SET_SUCCESS';
export const SET_ERROR_MESSAGE = 'SET_ERROR_MESSAGE';
export const SET_SUCCESS_MESSAGE = 'SET_SUCCESS_MESSAGE';
export const SET_ACCESS_TOKEN = 'SET_ACCESS_TOKEN';
export const SET_REFRESH_TOKEN = 'SET_REFRESH_TOKEN';

export const setAuthenticated = (authenticated) => ({
    type: SET_AUTHENTICATED,
    payload: authenticated,
});

export const setError = (error) => ({
    type: SET_ERROR,
    payload: error,
});

export const setErrorMessage = (message) => ({
    type: SET_ERROR_MESSAGE,
    payload: message,
});

export const setSuccess = (success) => ({
    type: SET_SUCCESS,
    payload: success,
});

export const setSuccessMessage = (message) => ({
    type: SET_SUCCESS_MESSAGE,
    payload: message,
});

export const setRole = (role) => ({
    type: SET_ROLE,
    payload: role,
});

export const setAccessToken = (accessToken) => ({
    type: SET_ACCESS_TOKEN,
    payload: accessToken,
});

export const setRefreshToken = (refreshToken) => ({
    type: SET_REFRESH_TOKEN,
    payload: refreshToken,
});