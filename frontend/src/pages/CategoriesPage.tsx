import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCategories } from "../services/api";
import type { Category } from "../types";
import { ErrorMessage, Loading } from "../components/Status";

// List of all categories
export default function CategoriesPage() {
  const [categories, setCategories] = useState<Category[] | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    getCategories()
      .then(setCategories)
      .catch((e: Error) => setError(e.message));
  }, []);

  if (error) return <ErrorMessage message={error} />;
  if (!categories) return <Loading />;

  return (
    <>
      <section className="hero small">
        <p className="eyebrow">Categories</p>
        <h1>Browse quizzes by topic.</h1>
      </section>

      {categories.length === 0 && <p className="empty">No categories yet.</p>}

      <div className="card-grid">
        {categories.map((category) => (
          <Link key={category.id} className="category-card" to={"/categories/" + category.id}>
            <h3>{category.name}</h3>
            <p>{category.description}</p>
            <span className="card-link">See quizzes →</span>
          </Link>
        ))}
      </div>
    </>
  );
}
