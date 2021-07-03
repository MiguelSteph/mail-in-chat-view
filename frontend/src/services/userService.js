import axios from "axios";
import config from "./config";

const userLoginWithGoogle = (authCode) => {
  return axios.post(config.loginEndPoint, encodeURIComponent(authCode)).then(
    (response) => {
      console.log(response);
    },
    (error) => {
      console.log(error);
    }
  );
};

export default {
  userLoginWithGoogle: userLoginWithGoogle,
};
