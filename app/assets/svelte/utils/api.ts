import { showToast } from '../stores/toast';

export async function fetchJson<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  try {
    const response = await fetch(endpoint, options);

    if (response.ok) {
      return await response.json() as T;
    }

    const error = await response.json();
    const message = error.message || `Error: ${response.status} ${response.statusText}`;
    showToast(message, 'danger');
    return null;
  } catch (error) {
    console.error('Fetch error:', error);
    showToast('A network error occurred.', 'danger');
    return null;
  }
}

export async function postJson<T>(endpoint: string, data: unknown): Promise<T | null> {
  try {
    const response = await fetch(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (response.ok) {
      const text = await response.text();
      return text ? (JSON.parse(text) as T) : null;
    }

    const error = await response.json();
    const message = error.message || `Error: ${response.status} ${response.statusText}`;
    showToast(message, 'danger');
    return null;
  } catch (error) {
    console.error('Post error:', error);
    showToast('A network error occurred.', 'danger');
    return null;
  }
}

export type PagedResult<T> = {
  items: T[];
  total: number;
}
