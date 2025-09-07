import { message as messageStore } from '../stores/message';

// APIエラーなど、構造化されたエラーレスポンスを想定
interface ApiError {
  response?: {
    data?: {
      message?: string;
    };
  };
  message: string;
}

function extractErrorMessage(error: unknown): string {
  const err = error as ApiError;
  return err.response?.data?.message || err.message || 'An unexpected error occurred.';
}

export function safeRun<T extends (...args: any[]) => Promise<any>>(fn: T) {
  return async function (...args: Parameters<T>): Promise<ReturnType<T> | void> {
    try {
      return await fn(...args);
    } catch (error) {
      console.error(error);
      const message = extractErrorMessage(error);
      messageStore.error(message);
    }
  } as T;
}
