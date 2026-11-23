import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';

// List of published quizzes. 
import { Link } from 'react-router-dom';

import { useEffect, useState } from 'react';
import type { Category, Quiz } from '../types';
import { getCategories, getPublishedQuizzes } from '../services/api';
import { formatDate } from '../utils';
import QuizCard from '../components/QuizCard';
import { ErrorMessage, Loading } from '../components/Status';
// // Front page of the student dashboard.
// type Quiz = {
//   name: string;
//   description: string;
//   courseCode: string;
//   category: string;
//   createdAt: string;
// };

//const Quizzes: Quiz[] = [];


export default function QuizListPage() {
  const [quizzes, setQuizzes] = useState<Quiz[] | null>(null);
  const [categories, setCategories] = useState<Category[]>([]);
  const [filter, setFilter] = useState("all");
  const [error, setError] = useState("");

  useEffect(() => {
    Promise.all([getPublishedQuizzes(), getCategories()])
    .then(([loadedQuizzes, loadedCategories]) => {
      setQuizzes(loadedQuizzes);
      setCategories(loadedCategories);
    })
    .catch((err: Error) => setError(err.message));
  }, []);

  if (error) return <ErrorMessage message={error} />;
  if (!quizzes) return <Loading />;

  const visibleQuizzes = quizzes.filter((quiz) => {
    if (filter === "all") return true;
    if (filter === "none") return quiz.category === null;
    return quiz.category !== null && String(quiz.category.id) === filter;
  });
  const uncategorizedCount = quizzes.filter((quiz) => quiz.category === null).length;

  function chip(key: string, label: string, count: number) {
    return (
      <button key={key} type="button" className={"chip" + (filter === key ? " active" : "")}
        aria-pressed={filter === key} onClick={() => setFilter(key)}>
        {label} <span className="chip-count">{count}</span>
      </button>
    );
  }

  return (
    <>
      <section className="hero">
        <p className="eyebrow">Student dashboard</p>
        <h1>Pick a quiz and<br />test what you know.</h1>
        <p>Answer questions from your courses and get instant feedback.</p>
      </section>

      <section className="chips" aria-label="Filter quizzes by category">
        {chip("all", "All", quizzes.length)}
        {categories.map((category) =>
          chip(String(category.id), category.name,
            quizzes.filter((quiz) => quiz.category?.id === category.id).length),
        )}
        {uncategorizedCount > 0 && chip("none", "Uncategorized", uncategorizedCount)}
      </section>

      <section className="panel">
        <h2>Quizzes</h2>
        
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell align="left">Description</TableCell>
                <TableCell align="left">Course</TableCell>
                <TableCell align="left">Category</TableCell>
                <TableCell align="left">Added on</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {visibleQuizzes.map((quiz) => (
                <TableRow
                  key={quiz.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    <Link to={"/quizzes/" + quiz.id}>{quiz.name}</Link> 
                  </TableCell>
                  <TableCell align="left">{quiz.description}</TableCell>
                  <TableCell align="left">{quiz.courseCode}</TableCell>
                  <TableCell align="left">{quiz.category?.name ?? "-"}</TableCell>
                  <TableCell align="left">{formatDate(quiz.createdAt)}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </section>

      {visibleQuizzes.length === 0 ? (
        <p className="empty">No published quizzes here yet.</p>
      ) : (
        <div className="card-grid">
          {visibleQuizzes.map((quiz) => (
            <QuizCard key={quiz.id} quiz={quiz} />
          ))}
        </div>
      )}
    </>
  );
}
