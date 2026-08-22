import { Link, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
export default function Layout() {
  const { user, logout } = useAuth();
  return (
    <>
      <header>
        <b>TaskFlow</b>
        <nav>
          <Link to="/">Projects</Link>
          <span>{user?.name}</span>
          <button onClick={logout}>Logout</button>
        </nav>
      </header>
      <main>
        <Outlet />
      </main>
    </>
  );
}
