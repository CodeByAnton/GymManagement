import { SET_AUTHENTICATED, SET_ROLE,
    SET_ACCESS_TOKEN, SET_REFRESH_TOKEN } from './actions';

const initialState = {
    authenticated: false,
    role: '',
    accessToken: '',
    refreshToken: '',
};

const authReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_AUTHENTICATED:
            return {
                ...state,
                authenticated: action.payload,
            };
        case SET_ROLE:
            return {
                ...state,
                role: action.payload,
            };
        case SET_ACCESS_TOKEN:
            return {
                ...state,
                accessToken: action.payload,
            };
        case SET_REFRESH_TOKEN:
            return {
                ...state,
                refreshToken: action.payload,
            };
        default:
            return state;
    }
};

export default authReducer;