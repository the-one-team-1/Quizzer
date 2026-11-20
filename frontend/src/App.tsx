import { NavLink, Route, Routes } from "react-router-dom";
import QuizListPage from "./pages/QuizListPage";
import QuizPage from "./pages/QuizPage";
// import ResultsPage from "./pages/ResultsPage";
import CategoriesPage from "./pages/CategoriesPage";
import CategoryPage from "./pages/CategoryPage";

export default function App() {
  return (
    <>
      <header>
        <NavLink to="/" className="brand">
          Quizzer<span>Student dashboard</span>
        </NavLink>
        <nav>
          { }
          <NavLink to="/" end>Quizzes</NavLink>
          <NavLink to="/categories">Categories</NavLink>
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<QuizListPage />} />
          <Route path="/quizzes/:quizId" element={<QuizPage />} />
          {/* <Route path="/quizzes/:quizId/results" element={<ResultsPage />} /> */}
          <Route path="/categories" element={<CategoriesPage />} />
          <Route path="/categories/:categoryId" element={<CategoryPage />} />
          <Route path="*" element={<p className="empty">Page not found.</p>} />
        </Routes>
      </main>

      <footer>Quizzer · Haaga-Helia Software Development Project</footer>
    </>
  );
}
