import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { api } from "../api/client";
export default function Project() {
  const { id } = useParams();
  const [p, setP] = useState(),
    [ts, setTs] = useState([]),
    [title, setTitle] = useState("");
  async function load() {
    const [a, b] = await Promise.all([
      api.get(`/projects/${id}`),
      api.get(`/projects/${id}/tasks`),
    ]);
    setP(a.data);
    setTs(b.data);
  }
  useEffect(() => {
    load();
  }, [id]);
  async function add(e) {
    e.preventDefault();
    if (!title.trim()) return;
    await api.post(`/projects/${id}/tasks`, { title, status: "TODO" });
    setTitle("");
    load();
  }
  async function next(t) {
    const s =
      t.status === "TODO"
        ? "IN_PROGRESS"
        : t.status === "IN_PROGRESS"
        ? "DONE"
        : "TODO";
    await api.put(`/projects/${id}/tasks/${t.id}`, {
      title: t.title,
      description: t.description,
      status: s,
    });
    load();
  }
  async function del(t) {
    await api.delete(`/projects/${id}/tasks/${t.id}`);
    load();
  }
  if (!p) return <p>Loading...</p>;
  return (
    <>
      <p>
        <Link to="/">← Projects</Link>
      </p>
      <h1>{p.name}</h1>
      <p>{p.description}</p>
      <section className="card">
        <form className="row" onSubmit={add}>
          <input
            placeholder="New task"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
          <button>Add task</button>
        </form>
      </section>
      <div className="grid">
        {ts.map((t) => (
          <article className="card" key={t.id}>
            <h3>{t.title}</h3>
            <span className="badge">{t.status}</span>
            <div>
              <button onClick={() => next(t)}>Next status</button>{" "}
              <button onClick={() => del(t)}>Delete</button>
            </div>
          </article>
        ))}
      </div>
    </>
  );
}
