import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";
import Register from "./pages/Register";
import Login from "./pages/Login";
import PrivateRoute from "./components/PrivateRoute";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />

        {/* Private */}
        <Route element={<PrivateRoute />}>
          <Route path="/" element={<App />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </StrictMode>
);
