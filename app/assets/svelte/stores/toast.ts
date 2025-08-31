import { writable } from 'svelte/store';

export const toast = writable({
  show: false,
  message: '',
  color: 'primary'
});

export const showToast = (message: string, color: 'primary' | 'secondary' | 'success' | 'danger' | 'warning' | 'info' | 'light' | 'dark' = 'primary') => {
  console.log(message)
  toast.set({ show: true, message, color });
};

export const hideToast = () => {
  toast.set({ show: false, message: '', color: 'primary' });
};
