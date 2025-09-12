import { useState } from "react";
import { loginUser } from "../api/AuthApi"; // bạn cần viết hàm này trong AuthApi
import { toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await loginUser({ email, password });
      if (res.status === 200) {
        toast.success("Đăng nhập thành công!");
        setEmail("");
        setPassword("");

        // // Ví dụ: chuyển hướng sang trang dashboard
        // window.location.href = "/";
      } else {
        toast.error("Đăng nhập thất bại!");
      }
    } catch (error: any) {
      toast.error(error.response?.data?.message || "Đăng nhập thất bại!");
    }
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        height: "100vh",
        width: "100vw",
        backgroundColor: "#f7f7f7",
      }}
    >
      <div
        style={{
          width: "360px",
          padding: "40px",
          background: "#fff",
          boxShadow: "0px 2px 10px rgba(0,0,0,0.1)",
          borderRadius: "8px",
        }}
      >

        {/* Tiêu đề */}
        <h2
          style={{
            marginBottom: "20px",
            textAlign: "center",
            color: "#333",
          }}
        >
          Login
        </h2>

        <form onSubmit={handleLogin}>
          {/* Email */}
          <div style={{ marginBottom: "15px" }}>
            <label
              style={{
                display: "block",
                marginBottom: "6px",
                fontWeight: "500",
                fontSize: "14px",
                color: "#333",
              }}
            >
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "10px",
                backgroundColor: "#f7f7f7",
                border: "1px solid #ddd",
                borderRadius: "4px",
                outline: "none",
                color: "#000",
                boxSizing: "border-box",
              }}
              onFocus={(e) => (e.target.style.border = "1px solid #007bff")}
              onBlur={(e) => (e.target.style.border = "1px solid #ddd")}
            />
          </div>

          {/* Password */}
          <div style={{ marginBottom: "20px" }}>
            <label
              style={{
                display: "block",
                marginBottom: "6px",
                fontWeight: "500",
                fontSize: "14px",
                color: "#333",
              }}
            >
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "10px",
                backgroundColor: "#f7f7f7",
                border: "1px solid #ddd",
                borderRadius: "4px",
                outline: "none",
                color: "#000",
                boxSizing: "border-box",
                marginBottom: "30px",
              }}
              onFocus={(e) => (e.target.style.border = "1px solid #007bff")}
              onBlur={(e) => (e.target.style.border = "1px solid #ddd")}
            />
          </div>

          {/* Submit */}
          <button
            type="submit"
            style={{
              width: "100%",
              padding: "10px",
              backgroundColor: "#007bff",
              color: "#fff",
              border: "none",
              borderRadius: "4px",
              cursor: "pointer",
              fontWeight: "bold",
              fontSize: "16px",
              boxSizing: "border-box",
            }}
          >
            Login
          </button>
        </form>

        {/* Links */}
        <div
          style={{
            marginTop: "15px",
            fontSize: "14px",
            textAlign: "center",
            boxSizing: "border-box",
          }}
        >
          <a href="/register" style={{ color: "#007bff", textDecoration: "none" }}>
            Create an account
          </a>
        </div>
      </div>

      <ToastContainer position="top-right" autoClose={3000} />
    </div>
  );
};

export default Login;
