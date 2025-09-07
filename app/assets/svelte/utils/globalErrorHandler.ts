import { message } from '../stores/message';

export function setupGlobalErrorHandler() {
  window.addEventListener('error', (event) => {
    message.error(`Unhandled error: ${event.message}`);
  });

  window.addEventListener('unhandledrejection', (event) => {
    message.error(`Unhandled promise rejection: ${event.reason}`);
  });
}
