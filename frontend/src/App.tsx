import { Route, Routes } from "react-router-dom";
import "./App.css";
import { ProfilePage } from "./pages/Profile/ProfilePage";
import { MyNavigation } from "./components/MyNavigation/MyNavigation";
import { HomePage } from "./pages/Home/HomePage";
import { TrainingPage } from "./pages/Training/TrainingPage";
import Header from "./components/Header/Header";

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/training" element={<TrainingPage />} />
      </Routes>

      <MyNavigation />
    </>
  );
}

export default App;
