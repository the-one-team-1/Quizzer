import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { getQuiz, getResults } from "../services/api";
import type { QuestionResult, Quiz } from "../types";
import { byDifficulty } from "../utils";
import { ErrorMessage, Loading } from "../components/Status";

// How all students have answered the questions of a quiz
export default function ResultsPage() {
  const quizId = Number(useParams().quizId);
  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [results, setResults] = useState<QuestionResult[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getQuiz(quizId), getResults(quizId)])
      .then(([loadedQuiz, loadedResults]) => {
        setQuiz(loadedQuiz);
        setResults(
          [...loadedResults].sort((a, b) => byDifficulty(a.questionDifficulty, b.questionDifficulty)),
        );
      })
      .catch((e: Error) => setError(e.message));
  }, [quizId]);

  if (error) return <ErrorMessage message={error} />;
  if (!quiz) return <Loading />;

  const totalAnswers = results.reduce((sum, r) => sum + r.totalAnswers, 0);
  const correctAnswers = results.reduce((sum, r) => sum + r.correctAnswers, 0);
  const wrongAnswers = results.reduce((sum, r) => sum + r.wrongAnswers, 0);

  return (
    <>
      <Link className="back" to={"/quizzes/" + quiz.id}>← Back to the quiz</Link>

      <section className="hero small">
        <p className="eyebrow">Results</p>
        <h1>{quiz.name}</h1>
      </section>

      <div className="stats">
        <div className="stat"><strong>{totalAnswers}</strong><span>Answers in total</span></div>
        <div className="stat"><strong>{correctAnswers}</strong><span>Correct</span></div>
        <div className="stat"><strong>{wrongAnswers}</strong><span>Wrong</span></div>
      </div>

      <section className="panel">
        <h2>Answers per question</h2>
        {results.length === 0 ? (
          <p className="empty">This quiz has no questions yet.</p>
        ) : (
          <div className="table-wrapper">
            <table>
              <thead>
                <tr>
                  <th>Question</th>
                  <th>Difficulty</th>
                  <th>Answers</th>
                  <th>Correct</th>
                  <th>Wrong</th>
                  <th>Correct rate</th>
                </tr>
              </thead>
              <tbody>
                {results.map((result) => (
                  <tr key={result.questionId}>
                    <td>{result.questionText}</td>
                    <td>
                      <span className={"badge " + result.questionDifficulty.toLowerCase()}>
                        {result.questionDifficulty}
                      </span>
                    </td>
                    <td>{result.totalAnswers}</td>
                    <td className="correct-text">{result.correctAnswers}</td>
                    <td className="wrong-text">{result.wrongAnswers}</td>
                    <td>
                      {result.totalAnswers === 0
                        ? "–"
                        : Math.round((result.correctAnswers / result.totalAnswers) * 100) + " %"}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </>
  );
}
