async function _fetch<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  const response = await fetch(endpoint, options);

  if (response.ok) {
    const text = await response.text();
    return text ? (JSON.parse(text) as T) : ({} as T); // Return empty object for null text
  }

  let message: string;
  try {
    const error = await response.json();
    // Handle our specific error structure from the backend
    if (error.errors) {
      message = JSON.stringify({ errors: error.errors });
    } else {
      message = error.message || `Error: ${response.status} ${response.statusText}`;
    }
  } catch (e) {
    message = `Error: ${response.status} ${response.statusText}`;
  }

  throw new Error(message);
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
    if (data instanceof FormData) {
      options.body = data;
    } else {
      headers['Content-Type'] = 'application/json';
      options.body = JSON.stringify(data);
    }
  }

  return _fetch(endpoint, options);
}

export function get<T>(endpoint: string, options?: RequestInit): Promise<T | null> {
  return _fetch(endpoint, options);
}

export function post<T>(endpoint: string, data: unknown): Promise<T | null> {
  return _request('POST', endpoint, data);
}

export function postForm<T>(endpoint: string, data: FormData): Promise<T | null> {
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