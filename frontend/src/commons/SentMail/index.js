import React, { useState } from "react";
import { Button, Modal } from "react-bootstrap";
import "./style.css";

const SentMail = ({ mail }) => {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const isTextContentType = mail.contentType === "text";

  const displayMailHeader = () => {
    return (
      <div>
        <div className="s-subject-section sent-title-content">
          <span className="sent-strong-title">SUBJECT: </span> {mail.subject}
        </div>
        <div className="s-to-section sent-title-content">
          <span className="sent-strong-title">TO: </span> {mail.to}
        </div>
        <div className="s-cc-section sent-title-content">
          <span className="sent-strong-title">CCs: </span> {mail.ccs}
        </div>
        <div className="sent-title-content">
          <span className="sent-strong-title">DATE & TIME: </span>
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
            ? "s-snippet-section sent-content display-linebreak"
            : "s-snippet-section sent-content"
        }
        dangerouslySetInnerHTML={{ __html: content }}
      ></div>
    );
  };

  return (
    <>
      <div className="sent-mail-wrapper">
        {displayMailHeader()}
        {displayMailBody(mail.snippet)}
        <div className="full-message-section">
          <button
            onClick={handleShow}
            type="button"
            className="btn btn-dark full-message-btn"
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

export default SentMail;
