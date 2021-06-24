import React from "react";
import "./style.css";
import LogoImg from "../../assets/images/logo.png";

const Logo = ({ customStyle }) => {
  return (
    <div className="logo-wrapper">
      <img className="logo-img" src={LogoImg} />
      <span className="logo-text color_blue" style={customStyle}>
        Easy Read
      </span>
    </div>
  );
};

export default Logo;
