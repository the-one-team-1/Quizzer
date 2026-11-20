import { Link } from "react-router-dom";
import type { Quiz } from "../types";
import { formatDate } from "../utils";

interface QuizTableProps {
  quizzes: Quiz[];
  showCategory?: boolean;
}

// Table of quizzes

// it is used on the quiz list page and on a category's page.
export default function QuizTable({ quizzes, showCategory = true }: QuizTableProps) {
  if (quizzes.length === 0) {
    return <p className="empty">No published quizzes yet.</p>;
  }

  return (
    <div className="table-wrapper">
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Course code</th>
            {showCategory && <th>Category</th>}
            <th>Added on</th>
            <th>Results</th>
          </tr>
        </thead>
        <tbody>
          {quizzes.map((quiz) => (
            <tr key={quiz.id}>
              <td>
                <Link className="quiz-name" to={"/quizzes/" + quiz.id}>{quiz.name}</Link>
              </td>
              <td>{quiz.description}</td>
              <td><span className="code">{quiz.courseCode}</span></td>
              {showCategory && (
                <td>
                  {quiz.category
                    ? <Link to={"/categories/" + quiz.category.id}>{quiz.category.name}</Link>
                    : <span className="muted">–</span>}
                </td>
              )}
              <td>{formatDate(quiz.createdAt)}</td>
              <td><Link to={"/quizzes/" + quiz.id + "/results"}>See results</Link></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
