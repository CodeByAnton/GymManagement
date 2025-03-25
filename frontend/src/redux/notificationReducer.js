import {
    SET_ERROR, SET_ERROR_MESSAGE,
    SET_SUCCESS, SET_SUCCESS_MESSAGE
} from './actions';

const initialNotificationState = {
    error: false,
    errorMessage: '',
    success: false,
    successMessage: '',
};

const notificationReducer = (state = initialNotificationState, action) => {
    switch (action.type) {
        case SET_ERROR:
            return {
                ...state,
                error: action.payload,
            };
        case SET_ERROR_MESSAGE:
            return {
                ...state,
                errorMessage: action.payload,
            };
        case SET_SUCCESS:
            return {
                ...state,
                success: action.payload,
            };
        case SET_SUCCESS_MESSAGE:
            return {
                ...state,
                successMessage: action.payload,
            };
        default:
            return state;
    }
};

export default notificationReducer;