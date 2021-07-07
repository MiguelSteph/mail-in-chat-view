import React, { useEffect } from "react";
import userService from "../../services/userService";
import "./style.css";

const EasyMailRead = (props) => {
  useEffect(() => {
    if (!userService.isLogged()) {
      props.history.replace("/");
    }
  });
  return <>Hey I am here looking for the next step.</>;
};

export default EasyMailRead;
