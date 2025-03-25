export const Providers = {
    LoginProvider: 'login-provider',
    RegisterProvider: 'register-provider',
    ChangePasswordProvider: 'change-password-provider',
    SubscriptionProvider: 'subscription-provider',
}
export const RegisterFlow = {
    RegisterForm: 'register-form',
    EmailVerification: 'email-verification',
    Application: 'application',
}
export const LoginFlow = {
    LoginForm: 'login-form',
    EmailVerification: 'email-verification'
}
export const ResetPasswordFlow = {
    ResetPasswordForm: 'reset-password-provider',
    EmailVerification: 'email-verification'
}

export const Role = {
    User: 'CLIENT',
    Admin: 'ADMINISTRATOR',
    Trainer: 'TRAINER'
}

export const Pages = {
    Login: 'login',
    Applications: 'applications',
    Subscription: 'subscription',
    Notification: 'notification',
    Search: 'search',
    Schedule: 'schedule',
    CreateTraining: 'create-training',
    ClientBooking: 'client-booking',
    ClientSchedule: 'client-schedule'
}


const AUTH_URL = '/api/v1/auth';
export const LOGIN_URL = AUTH_URL + '/login'
export const LOGOUT_URL = AUTH_URL + '/logout'
export const REGISTER_URL = AUTH_URL + '/register'
export const VERIFY_OTP_URL = AUTH_URL + '/verify-otp'
export const RESET_PASSWORD_URL = AUTH_URL + '/reset-password'
export const SEND_OTP_URL = AUTH_URL + '/send-otp/'
export const REFRESH_URL = AUTH_URL + '/refresh'

export const APPLICATIONS = '/api/v1/applications'
export const CONSIDER_APPLICATION = APPLICATIONS + '/consider'
export const ApplicationDecisions = {
    REJECT: 'REJECTED',
    ACCEPT: 'ACCEPTED',
}
export const SUBSCRIPTION_URL = '/api/v1/subscription';

export const NOTIFICATIONS_URL = '/api/v1/notifications';

export const PERSONAL_SERVICE_URL = '/api/v1/personal-services';
export const USER_INFO_URL = '/api/v1/user';
export const GROUP_TRAINING_URL = '/api/v1/group-training';
export const GROUP_TRAINING_PROGRAM_DETAILS_URL = '/api/v1/group-training-details';
const backgroundImageUrl = 'url(/img/sea.jpg)';

export const backgroundStyle = {
    backgroundImage: backgroundImageUrl,
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    height: '100vh',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center'
};