import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
// List of published quizzes. 

import { useEffect, useState } from 'react';
import type { Quiz } from '../types';
import { getPublishedQuizzes } from '../services/api';
import { formatDate } from '../utils';
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
  const [quizzes, setQuizzes] = useState<Quiz[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    getPublishedQuizzes()
    .then(setQuizzes)
    .catch((err: Error) => setError(err.message));
  }, []);

  return (
    <>
      <section className="hero">
        <p className="eyebrow">Student dashboard</p>
        <h1>Pick a quiz and<br />test what you know.</h1>
        <p>Answer questions from your courses and get instant feedback.</p>
      </section>

      <section className="panel">
        <h2>Quizzes</h2>
        
        {error && <p role="alert">{error}</p>}
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
              {quizzes.map((quiz) => (
                <TableRow
                  key={quiz.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {quiz.name}
                  </TableCell>
                  <TableCell align="right">{quiz.description}</TableCell>
                  <TableCell align="right">{quiz.courseCode}</TableCell>
                  <TableCell align="right">{quiz.category?.name ?? "-"}</TableCell>
                  <TableCell align="right">{formatDate(quiz.createdAt)}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </section>
    </>
  );
}
