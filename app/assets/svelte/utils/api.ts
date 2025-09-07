import { showToast } from '../stores/toast';

async function _fetch<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  try {
    const response = await fetch(endpoint, options);

    if (response.ok) {
      const text = await response.text();
      return text ? (JSON.parse(text) as T) : ({} as T); // Return empty object for null text
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

async function _request<T>(
  method: 'POST' | 'PUT' | 'DELETE',
  endpoint: string,
  data?: unknown
): Promise<T | null> {
  const csrf = document.getElementById('app')?.attributes.getNamedItem('csrf')?.value;
  const headers: HeadersInit = {};
  if (csrf) {
    headers['Csrf-Token'] = csrf;
  }

  const options: RequestInit = {
    method,
    headers,
  };

  if (data) {
    headers['Content-Type'] = 'application/json';
    options.body = JSON.stringify(data);
  }

  return _fetch(endpoint, options);
}

export function get<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  return _fetch(endpoint, options);
}

export function post<T>(endpoint: string, data: unknown): Promise<T | null> {
  return _request('POST', endpoint, data);
}

export function put<T>(endpoint: string, data: unknown): Promise<T | null> {
  return _request('PUT', endpoint, data);
}

export function del<T>(endpoint: string): Promise<T | null> {
  return _request('DELETE', endpoint);
}

export type PagedResult<T> = {
  items: T[];
  total: number;
};