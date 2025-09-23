import { readable } from 'svelte/store';
import { messages, type MessageKey } from '../generated/messages';

export const t = readable((key: MessageKey, ...args: any[]): string => {
  let message: string = messages[key] || key;
  // 引数があればプレースホルダーを置換 (例: {0}, {1})
  if (args.length > 0) {
    args.forEach((arg, i) => {
      message = message.replace(new RegExp(`\\{${i}\\}`, 'g'), arg);
    });
  }
  return message;
});


