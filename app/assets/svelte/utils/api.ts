import { showToast } from '../stores/toast';

async function _fetch<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  try {
    const response = await fetch(endpoint, options);

    if (response.ok) {
      const text = await response.text();
      return text ? (JSON.parse(text) as T) : null;
    }

    let message: string | undefined;
    try {
      const error = await response.json();
      message = error.message;
    } catch (e) {
      // ignore if response is not json
    }

    if (response.status >= 500) {
      showToast(message || '予期しないエラーが発生しました。', 'danger');
    } else {
      showToast(message || `Error: ${response.status} ${response.statusText}`, 'danger');
    }
    return null;
  } catch (error) {
    console.error('API call error:', error);
    showToast('A network error occurred.', 'danger');
    return null;
  }
}

export function fetchJson<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  return _fetch(endpoint, options);
}

export function postJson<T>(endpoint: string, data: unknown): Promise<T | null> {
  return _fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  });
}

export type PagedResult<T> = {
  items: T[];
  total: number;
}
