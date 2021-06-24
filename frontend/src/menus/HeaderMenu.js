import React from "react";
import "./style.css";
import LoginWithGoogle from "../commons/LoginWithGoogle";
import Logo from "../commons/Logo/index";

const HeaderMenu = () => {
  return (
    <nav>
      <Logo />
      <LoginWithGoogle />
    </nav>
  );
};

export default HeaderMenu;
