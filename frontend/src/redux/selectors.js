export const selectAuthenticated = (state) => state.auth.authenticated;
export const selectRole = (state) => state.auth.role;
export const selectAccessToken = (state) => state.auth.accessToken;
export const selectRefreshToken = (state) => state.auth.refreshToken;
export const selectError = (state) => state.notifications.error;
export const selectErrorMessage = (state) => state.notifications.errorMessage;
export const selectSuccess = (state) => state.notifications.success;
export const selectSuccessMessage = (state) => state.notifications.successMessage;
export const selectBucket = (state) => state.bucket.products;