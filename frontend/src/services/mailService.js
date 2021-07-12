import authEnhancedAx from "./authEnhancedAxios";
import config from "./config";

const _formatDate = (date) => {
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const monthStr = month < 10 ? "0" + month : month;
  const dayStr = day < 10 ? "0" + day : day;
  return date.getFullYear() + "-" + monthStr + "-" + dayStr;
};

const fetchMails = async (query) => {
  query.from = _formatDate(query.from);
  query.to = _formatDate(query.to);

  const response = await authEnhancedAx.post(config.fetchMailsEndPoint, query);

  return response.data;
};

export default {
  fetchMails,
};
