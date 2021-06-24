import React from "react";
import "./style.css";
import Logo from "../commons/Logo/index";
import MailIcon from "../assets/images/mail-icon.svg";

const Footer = () => {
  return (
    <div className="footer-wrapper">
      <ul>
        <li>
          <Logo customStyle={{ color: "#ffffff" }} />
        </li>
        <li>
          <address>
            Written by &nbsp;
            <a href="https://www.linkedin.com/in/kakanakou-miguel/">
              Miguel KAKANAKOU
            </a>
          </address>
        </li>
        <li>
          <address>
            <img src={MailIcon} />
            <a href="mailto:Skakanakou@gmail.com">Skakanakou@gmail.com</a>{" "}
          </address>
        </li>
      </ul>
    </div>
  );
};

export default Footer;
