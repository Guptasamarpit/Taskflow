import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { api } from "../api/client";
import { useAuth } from "../context/AuthContext";
export default function Register() {
  const [f, setF] = useState({ name: "", email: "", password: "" }),
    [err, setErr] = useState("");
  const { login } = useAuth(),
    nav = useNavigate();
  async function go(x) {
    x.preventDefault();
    try {
      console.log("registered data is",f);
      const { data } = await api.post("/auth/register", f);
      login(data);
      nav("/");
    } catch (x) {
      setErr(x.response?.data?.message || "Registration failed");
    }
  }
  return (
    <section className="card auth">
      <h1>Create account</h1>
      <form onSubmit={go}>
        {["name", "email", "password"].map((k) => (
          <input
            key={k}
            type={k === "password" ? "password" : "text"}
            placeholder={k}
            value={f[k]}
            onChange={(x) => setF({ ...f, [k]: x.target.value })}
          />
        ))}
        <button>Create account</button>
      </form>
      {err && <p className="error">{err}</p>}
      <p>
        Already registered? <Link to="/login">Login</Link>
      </p>
    </section>
  );
}
