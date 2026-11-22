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
  questionCount: number;
}

// The backend does not tell which option is correct until the answer is submitted
export interface AnswerOption {
  id: number;
  optionText: string;
}

export type Difficulty = "Easy" | "Normal" | "Hard";

export interface Question {
  id: number;
  questionText: string;
  difficulty: Difficulty;
  answerOptions: AnswerOption[];
}

// Reply of POST /api/answers
export interface AnswerResponse {
  correct: boolean;
  correctOptionIds: number[];
}

export interface QuestionResult {
  questionId: number;
  questionText: string;
  questionDifficulty: Difficulty;
  totalAnswers: number;
  correctAnswers: number;
  wrongAnswers: number;
}
