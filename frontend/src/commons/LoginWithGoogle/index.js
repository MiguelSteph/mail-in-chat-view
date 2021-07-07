import React, { useEffect } from "react";
import "./style.css";
import userService from "../../services/userService";

const LoginWithGoogle = ({ buttonId, handleSuccessfullLogin }) => {
  let auth2;
  useEffect(() => {
    loadGoogleApi();
  }, []);

  const loadGoogleApi = () => {
    window.gapi.load("auth2", () => {
      auth2 = window.gapi.auth2.init({
        client_id: process.env.REACT_APP_GOOGLE_CLIENT_ID,
        scope: "https://www.googleapis.com/auth/gmail.readonly",
      });
    });
  };

  const handleClickEvent = () => {
    if (!auth2) {
      loadGoogleApi();
    }
    auth2.grantOfflineAccess().then(onSuccessHandler, onFailureHandler);
  };

  const onSuccessHandler = async (response) => {
    await userService.userLoginWithGoogle(response.code);
    handleSuccessfullLogin();
  };

  const onFailureHandler = (error) => {
    console.error("Logging googler error", error);
  };

  return (
    <button
      onClick={handleClickEvent}
      id={buttonId}
      className="login-with-google"
    >
      <i className="fab fa-google fa-1x"></i>
      <span className="login-with-google-btn-text">Login with Google</span>
    </button>
  );
};

export default LoginWithGoogle;
