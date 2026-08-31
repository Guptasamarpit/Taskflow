import { useEffect, useRef, useState } from "react";

export default function CommentModal({ open, onClose, onSubmit, initialValue = "", activeTaskId, projectId }) {
  const [content, setContent] = useState(initialValue);
  const inputRef = useRef(null);

  useEffect(() => {
    if (open) {
      setContent(initialValue);
      requestAnimationFrame(() => inputRef.current?.focus());
    }
  }, [open, initialValue]);

  useEffect(() => {
    if (!open) return;

    const onKey = (e) => {
      if (e.key === "Escape") onClose();
    };

    window.addEventListener("keydown", onKey);
    return () => window.removeEventListener("keydown", onKey);
  }, [open, onClose]);

  if (!open) return null;

  const handleSubmit = (e) => {
    e.preventDefault();
    const trimmed = content.trim();
    if (!trimmed) return;
    onSubmit(trimmed);
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="comment-modal"
        role="dialog"
        aria-modal="true"
        aria-labelledby="comment-modal-title"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="comment-modal-header">
          <h3 id="comment-modal-title">Add Comment for project {projectId} and task {activeTaskId}</h3>
          <button
            type="button"
            className="close-button"
            onClick={onClose}
            aria-label="Close comment modal"
          >
            ×
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <textarea
            ref={inputRef}
            className="comment-textarea"
            rows={5}
            value={content}
            onChange={(e) => setContent(e.target.value)}
            placeholder="Write your comment here..."
            maxLength={2000}
          />

          <div className="comment-modal-actions">
            <button type="button" className="secondary-button" onClick={onClose}>
              Cancel
            </button>
            <button
              type="submit"
              className="primary-button"
              disabled={!content.trim()}
            >
              Save Comment
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}