import { useState, useEffect } from 'react';
import { fetchTasks } from '../api';

export function useTasks(query, status, page, pageSize) {
  const [tasks, setTasks] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [debouncedQuery, setDebouncedQuery] = useState(query);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      setDebouncedQuery(query);
    }, 300);

    return () => {
      clearTimeout(timeoutId);
    };
  }, [query]);

  useEffect(() => {
    const controller = new AbortController();

    setLoading(true);
    setError(null);

    // Loading error
    fetchTasks({ query: debouncedQuery, status, page, pageSize },
      { signal: controller.signal })
      .then((data) => {
        setTasks(data.items);
        setTotal(data.total);
      })
      .catch((err) => {
        if (err.name !== 'AbortError' &&
          err.name !== 'CanceledError') {
          setError(err.message || 'Failed to fetch tasks.');
        }
      }).finally(() => {
        if (!controller.signal.aborted) {
          setLoading(false);
        }
      });

    return () => {
      controller.abort();
    };

  }, [debouncedQuery, status, page, pageSize]);

  return { tasks, total, loading, error };
}
