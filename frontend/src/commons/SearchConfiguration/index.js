import React, { useState } from "react";
import "./style.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";

const SearchConfiguration = ({ handleFetchRequest }) => {
  const [dateRange, setDateRange] = useState([null, null]);
  const [startDate, endDate] = dateRange;
  const [dateRangeError, setDateRangeError] = useState(false);

  const [emailAddress, setEmailAddress] = useState("");
  const [emailAddressError, setEmailAddressError] = useState(false);

  const enableFetchBtn = () => {
    return startDate && endDate && emailAddress;
  };

  const validateEmail = (email) => {
    const re =
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!emailAddressError && !dateRangeError) {
      const query = {
        with: emailAddress,
        from: startDate,
        to: endDate,
      };
      handleFetchRequest(query);
    }
  };

  const handleEmailChange = ({ target }) => {
    setEmailAddress(target.value);
    if (!validateEmail(target.value)) {
      setEmailAddressError(true);
    } else {
      setEmailAddressError(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="search-config-wrapper">
        <div className="email-address-wrapper">
          <input
            type="email"
            className={
              emailAddressError
                ? "form-control is-invalid customize-input-text"
                : "form-control customize-input-text"
            }
            id="emailAddress"
            name="emailAddress"
            onChange={handleEmailChange}
            value={emailAddress}
            placeholder="Email Address"
            required
          />
          {emailAddressError && (
            <div id="ded" className="invalid-feedback">
              Please provide a valid email address.
            </div>
          )}
        </div>
        <div className="date-picker-wrapper">
          <DatePicker
            className={
              dateRangeError
                ? "form-control is-invalid customize-input-text"
                : "form-control customize-input-text"
            }
            placeholderText="Date Range"
            selectsRange={true}
            startDate={startDate}
            endDate={endDate}
            onChange={(update) => {
              setDateRange(update);
              if (update[0] == null || update[1] == null) {
                setDateRangeError(true);
              } else {
                setDateRangeError(false);
              }
            }}
            withPortal
          />
          {dateRangeError && (
            <div className="error-date-range">
              Please provide a valid date range.
            </div>
          )}
        </div>
        <div className="search-btn-wrapper">
          <button
            disabled={!enableFetchBtn()}
            type="submit"
            className="fetch-btn btn btn-outline-info"
          >
            Fetch
          </button>
        </div>
      </div>
    </form>
  );
};

export default SearchConfiguration;
