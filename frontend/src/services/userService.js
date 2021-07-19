import axios from "axios";
import jwtDecode from "jwt-decode";
import config from "./config";
import * as constant from "./constants";

const saveTokenToLocalStorage = (data) => {
  localStorage.setItem(constant.ACCESS_TOKEN, data.accessToken);
  localStorage.setItem(constant.REFRESH_TOKEN, data.refreshToken);
};

const clearTokenFromLocalStorage = () => {
  localStorage.removeItem(constant.ACCESS_TOKEN);
  localStorage.removeItem(constant.REFRESH_TOKEN);
};

const getAccessToken = () => {
  return localStorage.getItem(constant.ACCESS_TOKEN);
};

const getRefreshToken = () => {
  return localStorage.getItem(constant.REFRESH_TOKEN);
};

const userLoginWithGoogle = async (authCode) => {
  const { data } = await axios.post(
    config.loginEndPoint,
    encodeURIComponent(authCode)
  );
  if (data && data.accessToken) saveTokenToLocalStorage(data);
};

const renewAccessToken = async () => {
  try {
    const { data } = await axios.post(
      config.renewAccessTokenEndPoint,
      encodeURIComponent(getRefreshToken())
    );
    clearTokenFromLocalStorage();
    if (data && data.accessToken) saveTokenToLocalStorage(data);
  } catch (error) {
    console.log(error);
  }
};

const logout = () => {
  clearTokenFromLocalStorage();
};

const isLogged = () => {
  return getAccessToken() ? true : false;
};

const currentUser = () => {
  const decodedJwt = jwtDecode(getAccessToken());
  return [decodedJwt.sub, decodedJwt.picture];
};

const isTokenExpired = () => {
  const decodedJwt = jwtDecode(getAccessToken());
  return (decodedJwt.exp - 5000) * 1000 <= new Date().getTime();
};

const googleClientInfo = async () => {
  const { data } = await axios.get(config.googleClientInfoEndPoint);
  return data;
};

export default {
  userLoginWithGoogle,
  isLogged,
  isTokenExpired,
  currentUser,
  renewAccessToken,
  logout,
  getAccessToken,
  googleClientInfo,
};
