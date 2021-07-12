import React from "react";
import "./style.css";
import userService from "../../services/userService";
import SentMail from "../SentMail/index";
import ReceivedMail from "../ReceivedMail/index";

const FetchMailResult = ({ mailsList }) => {
  return (
    <div className="fetch-result-wrapper">
      {!mailsList && (
        <div className="alert alert-dark" role="alert">
          No mail to display.
        </div>
      )}

      {mailsList &&
        mailsList.map((mail) => {
          if (mail.from.indexOf(userService.currentUser()[0]) >= 0) {
            return <SentMail key={mail.dateTime} mail={mail} />;
          } else {
            return <ReceivedMail key={mail.dateTime} mail={mail} />;
          }
        })}
    </div>
  );
};

export default FetchMailResult;
