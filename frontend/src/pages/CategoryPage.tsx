import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getCategory, getCategoryQuizzes } from "../services/api";
import type { Category, Quiz } from "../types";
import QuizTable from "../components/QuizTable";
import { ErrorMessage, Loading } from "../components/Status";

//  See the quizzes of a category
export default function CategoryPage() {
  const categoryId = Number(useParams().categoryId);
  const [category, setCategory] = useState<Category | null>(null);
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getCategory(categoryId), getCategoryQuizzes(categoryId)])
      .then(([loadedCategory, loadedQuizzes]) => {
        setCategory(loadedCategory);
        setQuizzes(loadedQuizzes);
      })
      .catch((e: Error) => setError(e.message));
  }, [categoryId]);

  if (error) return <ErrorMessage message={error} />;
  if (!category) return <Loading />;

  return (
    <>
      <Link className="back" to="/categories">← All categories</Link>

      <section className="hero small">
        <p className="eyebrow">Category</p>
        <h1>{category.name}</h1>
        <p>{category.description}</p>
      </section>

      <section className="panel">
        <h2>Quizzes</h2>
        {/* in the same categoy so hidden*/}
        <QuizTable quizzes={quizzes} showCategory={false} />
      </section>
    </>
  );
}
