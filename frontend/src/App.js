import { Route, Switch } from "react-router";
import "./App.css";
import HeaderMenu from "./menus/HeaderMenu";
import routing from "./routing";
import Footer from "./menus/FooterPage";

function App() {
  return (
    <>
      <div className="container">
        <header className="App-header">
          <HeaderMenu />
        </header>
        <main className="App-main">
          <Switch>
            {routing.map((item) => (
              <Route
                key={item.name}
                path={item.path}
                component={item.component}
              />
            ))}
          </Switch>
        </main>
      </div>
      <footer className="App-footer">
        <Footer />
      </footer>
    </>
  );
}

export default App;
