import { Navigate, Outlet, useLocation } from "react-router-dom";
import { getAccessToken } from "../api/TokenService";

const PrivateRoute = () => {
  const token = getAccessToken();
  const location = useLocation();

  console.log("🔑 PrivateRoute check:", token);

  if (!token) {
    console.log("❌ Redirect to /login");
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  console.log("✅ Token found");
  return <Outlet />;
};

export default PrivateRoute;
