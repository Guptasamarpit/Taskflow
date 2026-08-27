import { Link, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
export default function Layout() {
  const { user, logout } = useAuth();
  return (
    <>
      <header>
        <b>TaskFlow</b>
        <nav>
          <Link to="/">Dashboard</Link>
          <Link to="/">Projects</Link>
          <Link to="/profile">Profile</Link>
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
