import { toast } from '../stores/toast';

export function setupGlobalErrorHandler() {
  window.addEventListener('error', (event) => {
    toast.error(`Unhandled error: ${event.message}`);
  });

  window.addEventListener('unhandledrejection', (event) => {
    toast.error(`Unhandled promise rejection: ${event.reason}`);
  });
}