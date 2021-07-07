import Home from "./pages/home/index";
import EasyMailRead from "./pages/mail-read";
import userService from "./services/userService";

const routing = [
  {
    name: "mail-easy-ready",
    path: "/mail-easy-ready",
    component: EasyMailRead,
  },
  {
    name: "home",
    path: "/",
    component: Home,
  },
];

export default routing;
