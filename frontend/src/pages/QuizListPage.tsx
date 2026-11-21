import { useEffect, useState } from "react";
import { getCategories, getQuizzes } from "../services/api";
import type { Category, Quiz } from "../types";
import QuizCard from "../components/QuizCard";
import { ErrorMessage, Loading } from "../components/Status";

// "all", "none" (no category) or a category id as text
type Filter = string;

// List of published quizzes with category filter chips.
// Front page of the student dashboard.
export default function QuizListPage() {
  const [quizzes, setQuizzes] = useState<Quiz[] | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [filter, setFilter] = useState<Filter>("all");
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getQuizzes(), getCategories()])
      .then(([loadedQuizzes, loadedCategories]) => {
        setQuizzes(loadedQuizzes);
        setCategories(loadedCategories);
      })
      .catch((e: Error) => setError(e.message));
  }, []);

  if (error) return <ErrorMessage message={error} />;
  if (!quizzes) return <Loading />;

  const uncategorizedCount = quizzes.filter((quiz) => quiz.category === null).length;

  const visibleQuizzes = quizzes.filter((quiz) => {
    if (filter === "all") return true;
    if (filter === "none") return quiz.category === null;
    return quiz.category !== null && String(quiz.category.id) === filter;
  });

  function chip(key: Filter, label: string, count: number) {
    return (
      <button
        key={key}
        type="button"
        className={"chip" + (filter === key ? " active" : "")}
        aria-pressed={filter === key}
        onClick={() => setFilter(key)}
      >
        {label} <span className="chip-count">{count}</span>
      </button>
    );
  }

  return (
    <>
      <section className="hero">
        <p className="eyebrow">Student dashboard</p>
        <h1>Pick a quiz and<br />test what you know.</h1>
        <p>Answer questions from your courses and get instant feedback.</p>
      </section>

      <section className="chips" aria-label="Filter quizzes by category">
        {chip("all", "All", quizzes.length)}
        {categories.map((category) =>
          chip(
            String(category.id),
            category.name,
            quizzes.filter((quiz) => quiz.category?.id === category.id).length,
          ),
        )}
        {uncategorizedCount > 0 && chip("none", "Uncategorized", uncategorizedCount)}
      </section>

      {visibleQuizzes.length === 0 ? (
        <p className="empty">No published quizzes here yet.</p>
      ) : (
        <div className="card-grid">
          {visibleQuizzes.map((quiz) => (
            <QuizCard key={quiz.id} quiz={quiz} />
          ))}
        </div>
      )}
    </>
  );
}
