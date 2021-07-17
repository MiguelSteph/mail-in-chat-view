const config = {
  loginEndPoint: process.env.REACT_APP_BACKEND_SERVER_URL + "/api/public/login",
  renewAccessTokenEndPoint:
    process.env.REACT_APP_BACKEND_SERVER_URL + "/api/public/auth/token",
  fetchMailsEndPoint:
    process.env.REACT_APP_BACKEND_SERVER_URL + "/api/private/mail",
  googleClientInfoEndPoint:
    process.env.REACT_APP_BACKEND_SERVER_URL + "/api/public/google/client/info",
};

export default config;
