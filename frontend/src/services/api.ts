// Communication with the backend REST API
// Components call these functions

import type { AnswerResponse, Category, Question, QuestionResult, Quiz } from "../types";

export const BACKEND_URL: string = import.meta.env.VITE_BACKEND_URL;

async function getJson<T>(path: string): Promise<T> {
  const response = await fetch(BACKEND_URL + path, { headers: { Accept: "application/json" } });
  if (!response.ok) {
    throw new Error(await errorMessage(response));
  }
  return response.json() as Promise<T>;
}

async function errorMessage(response: Response): Promise<string> {
  try {
    const body = await response.json();
    if (body.message) return body.message;
  } catch {
    // the reply had no JSON body
  }
  return "Request failed with status " + response.status;
}

export function getQuizzes(): Promise<Quiz[]> {
  return getJson<Quiz[]>("/api/quizzes");
}

export function getQuiz(quizId: number): Promise<Quiz> {
  return getJson<Quiz>("/api/quizzes/" + quizId);
}

export function getQuestions(quizId: number): Promise<Question[]> {
  return getJson<Question[]>("/api/quizzes/" + quizId + "/questions");
}

export function getPublishedQuizzes(): Promise<Quiz[]> {
  return getJson<Quiz[]>("/api/quizzes/published");
}

export async function submitAnswer(answerOptionId: number): Promise<AnswerResponse> {
  const response = await fetch(BACKEND_URL + "/api/answers", {
    method: "POST",
    headers: { Accept: "application/json", "Content-Type": "application/json" },
    body: JSON.stringify({ answerOptionId }),
  });
  if (!response.ok) {
    throw new Error(await errorMessage(response));
  }
  return response.json() as Promise<AnswerResponse>;
}

export function getResults(quizId: number): Promise<QuestionResult[]> {
  return getJson<QuestionResult[]>("/api/quizzes/" + quizId + "/results");
}

export function getCategories(): Promise<Category[]> {
  return getJson<Category[]>("/api/categories");
}

export function getCategory(categoryId: number): Promise<Category> {
  return getJson<Category>("/api/categories/" + categoryId);
}

export function getCategoryQuizzes(categoryId: number): Promise<Quiz[]> {
  return getJson<Quiz[]>("/api/categories/" + categoryId + "/quizzes");
}
