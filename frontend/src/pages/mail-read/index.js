import React, { useEffect, useState } from "react";
import userService from "../../services/userService";
import "./style.css";
import SearchConfiguration from "../../commons/SearchConfiguration/index";
import FetchMailResult from "../../commons/FetchMailResult/index";
import mailService from "../../services/mailService";

const EasyMailRead = (props) => {
  useEffect(() => {
    if (!userService.isLogged()) {
      props.history.replace("/");
    }
  });

  const [mailsList, setMailsList] = useState({});
  const [query, setQuery] = useState(null);
  const [isFetchingData, setFetchingData] = useState(false);

  const handleFetchRequest = async (fetchQuery) => {
    setFetchingData(true);
    setQuery(fetchQuery);
    const fetchedMails = await mailService.fetchMails({ ...fetchQuery });
    if (fetchedMails.hasOwnProperty("error")) {
      setMailsList(null);
    } else {
      setMailsList(fetchedMails);
    }
    setFetchingData(false);
  };

  const handlLoadMore = async () => {
    setFetchingData(true);
    const newFetchQuery = { ...query };
    newFetchQuery.pageToken = mailsList.pageToken;
    const newFetchedMails = await mailService.fetchMails(newFetchQuery);
    const newMailList = { ...mailsList };
    newMailList.pageToken = newFetchedMails.pageToken;
    newMailList.mails = newMailList.mails.concat(newFetchedMails.mails);
    setMailsList(newMailList);
    setFetchingData(false);
  };

  return (
    <div
      className={
        isFetchingData ? "disable-div mail-read-wrapper" : "mail-read-wrapper"
      }
    >
      {isFetchingData && (
        <div class="spinner-border text-info loader-wrapper" role="status">
          <span class="sr-only">Loading...</span>
        </div>
      )}

      <SearchConfiguration handleFetchRequest={handleFetchRequest} />
      <FetchMailResult mailsList={mailsList ? mailsList.mails : []} />
      {mailsList && mailsList.pageToken && (
        <div className="load-more">
          <button
            onClick={handlLoadMore}
            className="btn btn-light load-more-btn"
          >
            Load More...
          </button>
        </div>
      )}
    </div>
  );
};

export default EasyMailRead;
