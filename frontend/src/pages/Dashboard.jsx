import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client";
export default function Dashboard() {
  const [ps, setPs] = useState([]),
    [name, setName] = useState(""),
    [desc, setDesc] = useState("");
  const load = () => api.get("/projects").then((r) =>
  {
    console.log("reached to dashboard");
    setPs(r.data)
  }
);
   useEffect(() => {
    load();
  }, []); 
  async function add(e) {
    e.preventDefault();
    if (!name.trim()) return;
    await api.post("/projects", { name, description: desc });
    setName("");
    setDesc("");
    load();
  }
  async function del(id) {
    if (confirm("Delete project?")) {
      await api.delete(`/projects/${id}`);
      load();
    }
  }
  return (
    <>
      <h1>Projects</h1>
      <section className="card">
        <form className="row" onSubmit={add}>
          <input
            placeholder="Project name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
          <input
            placeholder="Description"
            value={desc}
            onChange={(e) => setDesc(e.target.value)}
          />
          <button>Create</button>
        </form>
      </section>
      <div className="grid">
        {ps.map((p) => (
          <article className="card" key={p.id}>
            <h2>
              <Link to={`/projects/${p.id}`}>{p.name}</Link>
            </h2>
            <p>{p.description}</p>
            <button onClick={() => del(p.id)}>Delete</button>
          </article>
        ))}
      </div>
    </>
  );
}
