import { useEffect, useState } from "react";
import { api } from "../api/client";
import { useAuth } from "../context/AuthContext";
export default function Profile() {
  const { login, user } = useAuth();
  const [f, setF] = useState({
    name: user?.name || "",
    email: user?.email || "",
  });
  const [msg, setMsg] = useState("");
  useEffect(() => {
    api
      .get("/me")
      .then((r) => setF({ name: r.data.name, email: r.data.email }));
  }, []);
  async function save(e) {
    e.preventDefault();
    const { data } = await api.put("/me", f);
    setF(data);
    setMsg("Profile updated.");
    const token = localStorage.getItem("token");
    login({ token, userId: data.id, name: data.name, email: data.email });
  }
  return (
    <section className="card auth">
      <h1>Profile</h1>
      <form onSubmit={save}>
        <input
          value={f.name}
          onChange={(e) => setF({ ...f, name: e.target.value })}
        />
        <input
          value={f.email}
          onChange={(e) => setF({ ...f, email: e.target.value })}
        />
        <button>Save</button>
      </form>
      {msg && <p>{msg}</p>}
    </section>
  );
}