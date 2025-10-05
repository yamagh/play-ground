<script lang="ts">
  import { onDestroy } from 'svelte';
  import { Button, Toast, ToastBody } from '@sveltestrap/sveltestrap';
  import { message as messageStore } from '../../stores/message';
  import type { Message } from '../../stores/message';

  let { showMessage = true } = $props<{ showMessage?: boolean }>();

  let messages = $state<Message[]>([]);
  const timers = new Map<number, number>();

  const removeMessage = (id: number) => {
    messageStore.remove(id);
  };

  const unsubscribe = showMessage
    ? messageStore.subscribe((newMessages) => {
        const newIds = new Set(newMessages.map((m) => m.id));
        const oldIds = new Set(messages.map((m) => m.id));

        // Clear timers for removed messages
        for (const id of oldIds) {
          if (!newIds.has(id) && timers.has(id)) {
            clearTimeout(timers.get(id));
            timers.delete(id);
          }
        }

        // Set timers for new messages
        for (const msg of newMessages) {
          if (!oldIds.has(msg.id) && msg.duration) {
            const timer = setTimeout(() => {
              removeMessage(msg.id);
            }, msg.duration);
            timers.set(msg.id, timer);
          }
        }
        messages = newMessages;
      })
    : () => {};

  onDestroy(() => {
    timers.forEach(clearTimeout);
    unsubscribe();
  });

  const colorMap = {
    success: 'success', error: 'danger', info: 'info', warning: 'warning',
    primary: 'primary', secondary: 'secondary', danger: 'danger', light: 'light', dark: 'dark'
  };
</script>

<div
  class="top-5 start-50 translate-middle-x position-fixed m--3"
  style="z-index: 1100"
>
  {#each messages as msg (msg.id)}
    {@const color = colorMap[msg.type] || 'primary'}
    <Toast isOpen={true} {color} class="text-bg-{color} mb-2">
      <div class="d-flex">
        <ToastBody>{msg.message}</ToastBody>
        <Button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          aria-label="Close"
          onclick={() => removeMessage(msg.id)}
        />
      </div>
    </Toast>
  {/each}
</div>
