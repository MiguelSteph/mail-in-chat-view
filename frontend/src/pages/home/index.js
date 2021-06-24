import React from "react";
import "./style.css";
import LoginWithGoogle from "../../commons/LoginWithGoogle/index";
import PresentationImg from "../../assets/images/easyReadPresentation.png";

const Home = () => {
  return (
    <>
      <div className="gray-div"></div>
      <div className="presentation-section-wrapper">
        <div className="presentation-text">
          <div className="presentation-header-section">
            <h1 className="presentation-text-header mobile-only-center-text color_blue">
              Reading emails never made so easy
            </h1>
          </div>
          <div className="presentation-text-section">
            <p className="presentation-text-content mobile-only-center-text color_blue">
              Reading all your emails threads as you read a chat discussion.
            </p>
          </div>
          <div className="presentation-login-button-wrapper">
            <LoginWithGoogle />
          </div>
        </div>
        <div className="presentation-img-wrapper">
          <img
            className="presentation-img"
            src={PresentationImg}
            alt="Presentation of easyread"
          />
        </div>
      </div>
    </>
  );
};

export default Home;
