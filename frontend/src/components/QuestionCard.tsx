import { useState } from "react";
import { submitAnswer } from "../services/api";
import type { Question } from "../types";

interface QuestionCardProps {
  question: Question;
  number: number; // position of the question on the page 
  // (starting from 1)
  total: number;
}

// Answer questions and get feedback
export default function QuestionCard({ question, number, total }: QuestionCardProps) {
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [feedback, setFeedback] = useState<{ correct: boolean; text: string } | null>(null);
  const [sending, setSending] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    if (selectedId === null) return;

    setSending(true);
    setError("");
    try {
      // The backend stores the answer 
      // it is counted on the results page
      await submitAnswer(selectedId);

      const chosen = question.answerOptions.find((option) => option.id === selectedId);
      if (chosen?.correct) {
        setFeedback({ correct: true, text: "That is correct, good job!" });
      } else {
        setFeedback({ correct: false, text: "That is not correct, try again." });
      }
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setSending(false);
    }
  }

  return (
    <form className="question-card" onSubmit={handleSubmit}>
      <div className="question-meta">
        <span>Question {number} of {total}</span>
        <span className={"badge " + question.difficulty.toLowerCase()}>{question.difficulty}</span>
      </div>
      <h3>{question.questionText}</h3>

      <fieldset>
        <legend className="visually-hidden">Answer options</legend>
        {question.answerOptions.map((option) => (
          <label key={option.id} className={"option" + (selectedId === option.id ? " selected" : "")}>
            <input
              type="radio"
              name={"question-" + question.id}
              value={option.id}
              checked={selectedId === option.id}
              onChange={() => {
                setSelectedId(option.id);
                setFeedback(null); // a new choice clears the o old feedback
              }}
            />
            {option.optionText}
          </label>
        ))}
      </fieldset>

      <button type="submit" disabled={selectedId === null || sending}>
        {sending ? "Sending…" : "Submit your answer"}
      </button>

      {feedback && (
        <p className={"feedback " + (feedback.correct ? "correct" : "wrong")} role="status">
          {feedback.text}
        </p>
      )}
      {error && <p className="feedback wrong" role="alert">{error}</p>}
    </form>
  );
}
