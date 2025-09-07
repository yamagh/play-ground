import { writable } from 'svelte/store';

export type Toast = {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'primary' | 'secondary' | 'danger' | 'warning' | 'light' | 'dark';
};

const { subscribe, update } = writable<Toast[]>([]);

let nextId = 0;

const add = (message: string, type: Toast['type'] = 'info') => {
  update((toasts) => {
    console.log('add toast', message, type);
    // To avoid displaying duplicate messages at the same time.
    if (toasts.some(toast => toast.message === message)) {
      return toasts;
    }
    return [...toasts, { id: nextId++, message, type }];
  });
};

const remove = (id: number) => {
  update((toasts) => toasts.filter((t) => t.id !== id));
};

export const toast = {
  subscribe,
  add,
  remove,
  success: (message: string) => add(message, 'success'),
  error: (message: string) => add(message, 'error'),
  info: (message: string) => add(message, 'info'),
  warning: (message: string) => add(message, 'warning'),
};