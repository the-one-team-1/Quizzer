import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getQuestions, getQuiz } from "../services/api";
import type { Question, Quiz } from "../types";
import QuestionCard from "../components/QuestionCard";
import { byDifficulty, formatDate } from "../utils";
import { ErrorMessage, Loading } from "../components/Status";

// See the quizs questions
export default function QuizPage() {
  const quizId = Number(useParams().quizId);
  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getQuiz(quizId), getQuestions(quizId)])
      .then(([loadedQuiz, loadedQuestions]) => {
        setQuiz(loadedQuiz);
        // From easiest to hardest
        setQuestions([...loadedQuestions].sort((a, b) => byDifficulty(a.difficulty, b.difficulty)));
      })
      .catch((e: Error) => setError(e.message));
  }, [quizId]);

  if (error) return <ErrorMessage message={error} />;
  if (!quiz) return <Loading />;

  return (
    <>
      <Link className="back" to="/">← All quizzes</Link>

      <section className="hero small">
        <p className="eyebrow">
          {quiz.courseCode}
          {quiz.category && <> · {quiz.category.name}</>}
          {" · Added on "}{formatDate(quiz.createdAt)}
        </p>
        <h1>{quiz.name}</h1>
        <p>{quiz.description}</p>
      </section>

      {questions.length === 0 && <p className="empty">This quiz has no questions yet.</p>}

      {questions.map((question, index) => (
        <QuestionCard key={question.id} question={question} number={index + 1} total={questions.length} />
      ))}

      {questions.length > 0 && (
        <p className="next-step">
          Done? <Link to={"/quizzes/" + quiz.id + "/results"}>See the results of this quiz →</Link>
        </p>
      )}
    </>
  );
}
