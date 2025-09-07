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

function _sendJson<T>(method: 'POST' | 'PUT', endpoint: string, data: unknown): Promise<T | null> {
  const csrf = document.getElementById("app")?.attributes.getNamedItem("csrf")?.value;
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
  };
  if (csrf) {
    headers['Csrf-Token'] = csrf;
  }
  return _fetch(endpoint, {
    method,
    headers,
    body: JSON.stringify(data),
  });
}

export function postJson<T>(endpoint: string, data: unknown): Promise<T | null> {
  return _sendJson('POST', endpoint, data);
}

export function putJson<T>(endpoint: string, data: unknown): Promise<T | null> {
  return _sendJson('PUT', endpoint, data);
}

export type PagedResult<T> = {
  items: T[];
  total: number;
}
