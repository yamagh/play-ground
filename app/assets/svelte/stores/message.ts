import { writable } from 'svelte/store';

export type Message = {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'primary' | 'secondary' | 'danger' | 'warning' | 'light' | 'dark';
};

const { subscribe, update } = writable<Message[]>([]);

let nextId = 0;

const add = (message: string, type: Message['type'] = 'info') => {
  update((messages) => {
    // To avoid displaying duplicate messages at the same time.
    if (messages.some(msg => msg.message === message)) {
      return messages;
    }
    return [...messages, { id: nextId++, message, type }];
  });
};

const remove = (id: number) => {
  update((messages) => messages.filter((t) => t.id !== id));
};

export const message = {
  subscribe,
  add,
  remove,
  success: (message: string) => add(message, 'success'),
  error: (message: string) => add(message, 'error'),
  info: (message: string) => add(message, 'info'),
  warning: (message: string) => add(message, 'warning'),
};
