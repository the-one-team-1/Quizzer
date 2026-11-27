import { useState } from "react";
import { submitAnswer } from "../services/api";
import type { AnswerResponse, Question } from "../types";

interface QuestionCardProps {
  question: Question;
  number: number; // position of the question (starting from 1)
  total: number;
  onAnswered: (correct: boolean) => void;
}

// Answer one question and get feedback.
// Answers can be changed and resubmitted until moving to the next question.
export default function QuestionCard({ question, number, total, onAnswered }: QuestionCardProps) {
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const [submittedId, setSubmittedId] = useState<number | null>(null);
  const [result, setResult] = useState<AnswerResponse | null>(null);
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  async function handleSubmit(event: React.FormEvent) {
    event.preventDefault();
    if (selectedId === null || isSubmitting) return;

    setIsSubmitting(true);
    setError("");
    try {
      // The backend stores the answer and tells if it was correct
      const response = await submitAnswer(selectedId);
      setResult(response);
      setSubmittedId(selectedId);
      onAnswered(response.correct);
    } catch (e) {
      setError((e as Error).message);
    } finally {
      setIsSubmitting(false);
    }
  }

  function optionClass(optionId: number): string {
    let css = "option";
    if (result && submittedId === optionId) {
      if (result.correct && result.correctOptionIds.includes(optionId)) css += " correct-option";
      else if (!result.correct) css += " wrong-option";
    }
    if (optionId === selectedId) {
      css += " selected";
    }
    return css;
  }

  return (
    <form className="question-card" onSubmit={handleSubmit}>
      <div className="question-meta">
        <span>Question {number} of {total}</span>
        <span className={"badge " + question.difficulty.toLowerCase()}>{question.difficulty}</span>
      </div>
      <h3>{question.questionText}</h3>

      <fieldset disabled={isSubmitting}>
        <legend className="visually-hidden">Answer options</legend>
        {question.answerOptions.map((option) => (
          <label key={option.id} className={optionClass(option.id)}>
            <input
              type="radio"
              name={"question-" + question.id}
              value={option.id}
              checked={selectedId === option.id}
              onChange={() => setSelectedId(option.id)}
            />
            {option.optionText}
          </label>
        ))}
      </fieldset>

      <button type="submit" disabled={selectedId === null || isSubmitting}>
        {isSubmitting ? "Sending…" : "Submit your answer"}
      </button>

      {result && (
        <p className={"feedback " + (result.correct ? "correct" : "wrong")} role="status">
          {result.correct
            ? "That is correct, good job!"
            : "That is not correct."}
        </p>
      )}
      {error && <p className="feedback wrong" role="alert">{error}</p>}
    </form>
  );
}
