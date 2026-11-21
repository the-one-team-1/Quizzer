import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getQuestions, getQuiz } from "../services/api";
import type { Question, Quiz } from "../types";
import QuestionCard from "../components/QuestionCard";
import { byDifficulty, formatDate } from "../utils";
import { ErrorMessage, Loading } from "../components/Status";

// Take a quiz: one question at a time, then a summary of your own score
export default function QuizPage() {
  const quizId = Number(useParams().quizId);
  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [questions, setQuestions] = useState<Question[]>([]);
  const [current, setCurrent] = useState(0);
  const [answered, setAnswered] = useState(false);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);
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

  const total = questions.length;
  const doneCount = finished ? total : current + (answered ? 1 : 0);
  const percent = total === 0 ? 0 : Math.round((doneCount / total) * 100);
  const isLast = current + 1 >= total;

  function handleAnswered(correct: boolean) {
    setAnswered(true);
    if (correct) setScore((value) => value + 1);
  }

  function handleNext() {
    if (isLast) {
      setFinished(true);
    } else {
      setCurrent(current + 1);
      setAnswered(false);
    }
  }

  function handleRestart() {
    setCurrent(0);
    setAnswered(false);
    setScore(0);
    setFinished(false);
  }

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

      {total === 0 && <p className="empty">This quiz has no questions yet.</p>}

      {total > 0 && (
        <div
          className="progress"
          role="progressbar"
          aria-label="Quiz progress"
          aria-valuemin={0}
          aria-valuemax={100}
          aria-valuenow={percent}
        >
          <div style={{ width: percent + "%" }} />
        </div>
      )}

      {total > 0 && !finished && (
        <>
          <QuestionCard
            key={questions[current].id}
            question={questions[current]}
            number={current + 1}
            total={total}
            onAnswered={handleAnswered}
          />
          {answered && (
            <div className="quiz-actions">
              <button type="button" onClick={handleNext}>
                {isLast ? "Finish quiz" : "Next question →"}
              </button>
            </div>
          )}
        </>
      )}

      {finished && (
        <section className="panel">
          <h2>Quiz finished</h2>
          <div className="stats">
            <div className="stat"><strong>{total}</strong><span>Questions</span></div>
            <div className="stat"><strong>{score}</strong><span>Correct</span></div>
            <div className="stat"><strong>{total - score}</strong><span>Wrong</span></div>
          </div>
          <p className="next-step">
            <button type="button" onClick={handleRestart}>Try again</button>
          </p>
          <p className="next-step">
            <Link to={"/quizzes/" + quiz.id + "/results"}>See how everyone answered →</Link>
          </p>
        </section>
      )}
    </>
  );
}
