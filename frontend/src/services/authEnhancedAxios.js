import axios from "axios";
import userService from "./userService";

const authEnhancedAx = axios.create();
authEnhancedAx.interceptors.request.use(async function (config) {
  if (userService.isTokenExpired()) {
    await userService.renewAccessToken();
  }
  config.headers.Authorization = "Bearer " + userService.getAccessToken();
  return config;
});

export default authEnhancedAx;
