import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../api/client";
export default function Dashboard() {
  const [ps, setPs] = useState([]),
    [s, setS] = useState({}),
    [name, setName] = useState(""),
    [desc, setDesc] = useState(""),
    [search, setSearch] = useState("");
  const load = async () => {
    const [a, b] = await Promise.all([
      api.get("/dashboard/summary"),
      api.get("/projects", { params: { search: search || undefined } }),
    ]);
    console.log("Ertrer",a.data, b.data);
    setS(a.data);
    setPs(b.data);
  };
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
     <h1>Dashboard</h1>
           {s && (
        <div className="stats">
          <div className="stat">
            <b>{s.projectCount}</b>
            <span>Projects</span>
          </div>
          <div className="stat">
            <b>{s.taskCount}</b>
            <span>Tasks</span>
          </div>
          <div className="stat">
            <b>{s.todoCount}</b>
            <span>Todo</span>
          </div>
          <div className="stat">
            <b>{s.inProgressCount}</b>
            <span>In progress</span>
          </div>
          <div className="stat">
            <b>{s.doneCount}</b>
            <span>Done</span>
          </div>
          <div className="stat">
            <b>{s.highPriorityCount}</b>
            <span>High priority</span>
          </div>
        </div>
      )}
      <section className="card">
        <h2>Create project</h2>
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
      <section>
          <div className="toolbar">
          <h2>Projects</h2>
          <input
            placeholder="Search projects"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && load()}
          />
          <button onClick={load}>Search</button>
        </div>
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
      </section>
    </>
  );
}
