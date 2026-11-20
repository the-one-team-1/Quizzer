// List of published quizzes. 
// Front page of the student dashboard.
export default function QuizListPage() {
  return (
    <>
      <section className="hero">
        <p className="eyebrow">Student dashboard</p>
        <h1>Pick a quiz and<br />test what you know.</h1>
        <p>Answer questions from your courses and get instant feedback.</p>
      </section>

      <section className="panel">
        <h2>Quizzes</h2>
        <p className="empty">The quiz list is coming soon. Meanwhile, browse quizzes by category.</p>
      </section>
    </>
  );
}
