import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { api } from "../api/client";
import CommentModal from "../components/CommentModal";

export default function Project() {
  const { id } = useParams();
  const [p, setP] = useState(),
    [ts, setTs] = useState([]),
    [title, setTitle] = useState(""),
    [desc, setDesc] = useState(""),
    [priority, setPriority] = useState("MEDIUM"),
    [dueDate, setDueDate] = useState(""),
    [filter, setFilter] = useState(""),
    [comments, setComments] = useState({}),
    [commentModalOpen, setCommentModalOpen] = useState(false),
    [activeTaskId, setActiveTaskId] = useState(null);

  const load = async () => {
    const [a, b] = await Promise.all([
      api.get(`/projects/${id}`),
      api.get(`/projects/${id}/tasks`, {
        params: filter ? { status: filter } : undefined,
      }),
    ]);

    setP(a.data);
    setTs(b.data);

    const entries = await Promise.all(
      b.data.map(async (t) => [
        t.id,
        (await api.get(`/projects/${id}/tasks/${t.id}/comments`)).data,
      ]),
    );

    setComments(Object.fromEntries(entries));
  };

  useEffect(() => {
    load().catch((error) => {
      console.error("Failed to load project comments:", error);
    });
  }, [id, filter]);

  async function add(e) {
    e.preventDefault();
    if (!title.trim()) return;

    await api.post(`/projects/${id}/tasks`, {
      title,
      description: desc,
      status: "TODO",
      priority,
      dueDate: dueDate || null,
    });

    setTitle("");
    setDesc("");
    setDueDate("");
    load();
  }

  async function next(t) {
    const status =
      t.status === "TODO"
        ? "IN_PROGRESS"
        : t.status === "IN_PROGRESS"
          ? "DONE"
          : "TODO";

    await api.put(`/projects/${id}/tasks/${t.id}`, {
      title: t.title,
      description: t.description,
      status,
      priority: t.priority,
      dueDate: t.dueDate,
    });

    load();
  }

  async function del(t) {
    await api.delete(`/projects/${id}/tasks/${t.id}`);
    load();
  }

  const openCommentModal = (tid) => {
    setActiveTaskId(tid);
    setCommentModalOpen(true);
  };

  const submitComment = async (content) => {
    if (!activeTaskId) return;

    await api.post(`/projects/${id}/tasks/${activeTaskId}/comments`, {
      content,
    });
    setCommentModalOpen(false);
    setActiveTaskId(null);
    load();
  };

  if (!p) return <p>Loading...</p>;

  return (
    <>
      <p>
        <Link to="/">← Dashboard</Link>
      </p>

      <div className="between">
        <div>
          <h1>{p.name}</h1>
          <p>{p.description}</p>
        </div>
        <span className="badge">{p.status}</span>
      </div>
      <section className="card">
        <h2>Add task</h2>
        <form className="grid-form" onSubmit={add}>
          <input
            placeholder="Title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
          <input
            placeholder="Description"
            value={desc}
            onChange={(e) => setDesc(e.target.value)}
          />
          <select
            value={priority}
            onChange={(e) => setPriority(e.target.value)}
          >
            <option>LOW</option>
            <option>MEDIUM</option>
            <option>HIGH</option>
          </select>
          <input
            type="date"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
          />
          <button>Add task</button>
        </form>
      </section>
      <div className="toolbar">
        <h2>Tasks</h2>
        <select value={filter} onChange={(e) => setFilter(e.target.value)}>
          <option value="">All</option>
          <option value="TODO">Todo</option>
          <option value="IN_PROGRESS">In progress</option>
          <option value="DONE">Done</option>
        </select>
      </div>

      <div className="grid">
        {ts.map((t) => (
          <article className="card" key={t.id}>
            <div className="between">
              <h3>{t.title}</h3>
              <span className="badge">{t.priority}</span>
            </div>
            <p>{t.description}</p>
            <p>
              Status: <b>{t.status}</b>
              {t.dueDate && <> · Due: {t.dueDate}</>}
            </p>
            <button onClick={() => next(t)}>Next status</button>{" "}
            <button onClick={() => openCommentModal(t.id)}>Comment</button>{" "}
            <button onClick={() => del(t)}>Delete</button>
            {comments[t.id]?.length > 0 && (
              <div className="comments">
                <h4>Comments</h4>
                {comments[t.id].map((c) => (
                  <p key={c.id}>
                    <b>{c.authorName}:</b> {c.content}
                  </p>
                ))}
              </div>
            )}
          </article>
        ))}
      </div>

      <CommentModal
        open={commentModalOpen}
        onClose={() => {
          setCommentModalOpen(false);
          setActiveTaskId(null);
        }}
        activeTaskId={activeTaskId}
        projectId={id}
        onSubmit={submitComment}
      />
    </>
  );
}
