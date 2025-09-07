import { writable } from 'svelte/store';

export type Message = {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'primary' | 'secondary' | 'danger' | 'warning' | 'light' | 'dark';
  duration?: number; // Duration in ms. undefined means it won't auto-close.
};

type AddMessageOptions = {
  duration?: number;
};

const STORAGE_KEY = 'user-messages';

const getInitialMessages = (): Message[] => {
  if (typeof window === 'undefined') return [];
  const stored = sessionStorage.getItem(STORAGE_KEY);
  return stored ? JSON.parse(stored) : [];
};

const initialMessages = getInitialMessages();
const { subscribe, set, update } = writable<Message[]>(initialMessages);

let nextId = initialMessages.length > 0 ? Math.max(...initialMessages.map(m => m.id)) + 1 : 0;

subscribe((messages) => {
  if (typeof window !== 'undefined') {
    if (messages.length === 0) {
      sessionStorage.removeItem(STORAGE_KEY);
    } else {
      sessionStorage.setItem(STORAGE_KEY, JSON.stringify(messages));
    }
  }
});

const add = (message: string, type: Message['type'] = 'info', options?: AddMessageOptions) => {
  update((messages) => {
    if (messages.some(msg => msg.message === message)) {
      return messages;
    }
    
    let duration: number | undefined = options?.duration === 0 ? 0 : options?.duration || 5000;
    if (type === 'error' || type === 'warning') {
      duration = options?.duration === undefined ? undefined : options.duration;
    }

    return [...messages, { id: nextId++, message, type, duration }];
  });
};

const remove = (id: number) => {
  update((messages) => messages.filter((t) => t.id !== id));
};

export const message = {
  subscribe,
  add,
  remove,
  success: (message: string, options?: AddMessageOptions) => add(message, 'success', options),
  error: (message: string, options?: AddMessageOptions) => add(message, 'error', options),
  info: (message: string, options?: AddMessageOptions) => add(message, 'info', options),
  warning: (message: string, options?: AddMessageOptions) => add(message, 'warning', options),
};
