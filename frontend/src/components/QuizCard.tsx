import { Link } from "react-router-dom";
import type { Quiz } from "../types";

// One quiz as a card on the front page
export default function QuizCard({ quiz }: { quiz: Quiz }) {
  return (
    <Link className="quiz-card" to={"/quizzes/" + quiz.id}>
      <div className="quiz-card-top">
        <span className="code">{quiz.courseCode}</span>
        <span className="muted">{quiz.category ? quiz.category.name : "Uncategorized"}</span>
      </div>
      <h3>{quiz.name}</h3>
      <p>{quiz.description}</p>
      <div className="quiz-card-footer">
        <span className="muted">
          {quiz.questionCount} {quiz.questionCount === 1 ? "question" : "questions"}
        </span>
        <span className="card-link">Start quiz →</span>
      </div>
    </Link>
  );
}
