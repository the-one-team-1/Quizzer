import type { Difficulty } from "./types";

export function formatDate(isoDate: string): string {
  return new Date(isoDate).toLocaleDateString("fi-FI", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
  });
}
const DIFFICULTY_ORDER: Record<Difficulty, number> = { Easy: 1, Normal: 2, Hard: 3 };

export function byDifficulty(a: Difficulty, b: Difficulty): number {
  return DIFFICULTY_ORDER[a] - DIFFICULTY_ORDER[b];
}
