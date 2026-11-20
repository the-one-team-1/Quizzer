export interface Category {
  id: number;
  name: string;
  description: string;
}

export interface Quiz {
  id: number;
  name: string;
  description: string;
  courseCode: string;
  published: boolean;
  createdAt: string;
  category: Category | null;
}

export interface AnswerOption {
  id: number;
  optionText: string;
  correct: boolean;
}

export type Difficulty = "Easy" | "Normal" | "Hard";

export interface Question {
  id: number;
  questionText: string;
  difficulty: Difficulty;
  answerOptions: AnswerOption[];
}

export interface QuestionResult {
  questionId: number;
  questionText: string;
  questionDifficulty: Difficulty;
  totalAnswers: number;
  correctAnswers: number;
  wrongAnswers: number;
}
