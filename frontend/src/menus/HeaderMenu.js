import React from "react";
import "./style.css";
import LoginWithGoogle from "../commons/LoginWithGoogle";
import Logo from "../commons/Logo/index";
import userService from "../services/userService";
import { withRouter } from "react-router";

const HeaderMenu = (props) => {
  const isUserLogged = userService.isLogged();
  const currentUser = isUserLogged ? userService.currentUser() : [];
  const onSuccessfullLogin = () => {
    if (userService.isLogged()) {
      props.history.push("/mail-easy-ready");
    }
  };

  const handleLogout = () => {
    userService.logout();
    props.history.push("/");
  };

  return (
    <nav>
      <Logo />
      {!isUserLogged && (
        <LoginWithGoogle
          buttonId="loginWithGoogleFromNavBar"
          handleSuccessfullLogin={onSuccessfullLogin}
        />
      )}
      {isUserLogged && (
        <div className="dropdown">
          <a
            className="nav-link dropdown-toggle username-menu"
            href="#"
            id="navbarDropdown"
            role="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            <img src={currentUser[1]} />
            <span>{currentUser[0]}</span>
          </a>
          <ul
            className="dropdown-menu dropdown-menu-dark"
            aria-labelledby="navbarDropdown"
          >
            <li>
              <a className="dropdown-item" href="#" onClick={handleLogout}>
                Logout
              </a>
            </li>
          </ul>
        </div>
      )}
    </nav>
  );
};

export default withRouter(HeaderMenu);
