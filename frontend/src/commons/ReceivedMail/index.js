import React, { useState } from "react";
import { Button, Modal } from "react-bootstrap";
import "./style.css";

const ReceivedMail = ({ mail }) => {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const isTextContentType = mail.contentType === "text";

  const displayMailHeader = () => {
    return (
      <div>
        <div className="subject-section received-title-content">
          <span className="received-strong-title">SUBJECT: </span>{" "}
          {mail.subject}
        </div>
        <div className="from-section received-title-content">
          <span className="received-strong-title">FROM: </span> {mail.from}
        </div>
        <div className="to-section received-title-content">
          <span className="received-strong-title">TO: </span> {mail.to}
        </div>
        <div className="cc-section received-title-content">
          <span className="received-strong-title">CCs: </span> {mail.ccs}
        </div>
        <div className="received-title-content">
          <span className="received-strong-title">DATE & TIME: </span>
          {mail.dateTime}
        </div>
      </div>
    );
  };

  const displayMailBody = (content) => {
    return (
      <div
        className={
          isTextContentType
            ? "snippet-section received-content display-linebreak"
            : "snippet-section received-content"
        }
        dangerouslySetInnerHTML={{ __html: content }}
      ></div>
    );
  };

  return (
    <>
      <div className="received-mail-wrapper">
        {displayMailHeader()}
        {displayMailBody(mail.snippet)}
        <div className="full-message-section">
          <button
            onClick={handleShow}
            type="button"
            className="btn btn-light full-message-btn"
          >
            View Full Mail
          </button>
        </div>
      </div>

      <Modal show={show} onHide={handleClose}>
        <Modal.Header>{displayMailHeader()}</Modal.Header>
        <Modal.Body>{displayMailBody(mail.content)}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ReceivedMail;
