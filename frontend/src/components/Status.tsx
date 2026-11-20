
export function Loading() {
  return <p className="status">Loading…</p>;
}

export function ErrorMessage({ message }: { message: string }) {
  return <p className="status error" role="alert">{message}</p>;
}
