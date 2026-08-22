import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { api } from "../api/client";
import { useAuth } from "../context/AuthContext";
export default function Login() {
  const [e, setE] = useState(""),
    [p, setP] = useState(""),
    [err, setErr] = useState("");
  const { login } = useAuth(),
    nav = useNavigate();
  async function go(x) {
    x.preventDefault();
    try {
      const { data } = await api.post("/auth/login", { email: e, password: p });
      login(data);
      nav("/");
    } catch (x) {
      setErr(x.response?.data?.message || "Login failed");
    }
  }
  return (
    <section className="card auth">
      <h1>Login</h1>
      <form onSubmit={go}>
        <input
          placeholder="Email"
          value={e}
          onChange={(x) => setE(x.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={p}
          onChange={(x) => setP(x.target.value)}
        />
        <button>Login</button>
      </form>
      {err && <p className="error">{err}</p>}
      <p>
        No account? <Link to="/register">Register</Link>
      </p>
    </section>
  );
}
