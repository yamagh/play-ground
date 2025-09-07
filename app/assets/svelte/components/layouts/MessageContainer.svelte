<script lang="ts">
  import { Button, Toast, ToastBody } from '@sveltestrap/sveltestrap';
  import { message as messageStore } from '../../stores/message';
  import type { Message } from '../../stores/message';

  let messages: Message[] = [];

  messageStore.subscribe((value) => {
    messages = value;
  });

  const removeMessage = (id: number) => {
    messageStore.remove(id);
  };

  const colorMap = {
    success: 'success',
    error: 'danger',
    info: 'info',
    warning: 'warning',
    primary: 'primary',
    secondary: 'secondary',
    danger: 'danger',
    light: 'light',
    dark: 'dark'
  };
</script>

<div
  class="top-0 start-50 translate-middle-x position-fixed m--3"
  style="z-index: 1100"
>
  {#each messages as { id, message, type } (id)}
    {@const color = colorMap[type] || 'primary'}
    <Toast isOpen={true} {color} class="text-bg-{color} mb-2">
      <div class="d-flex">
        <ToastBody>{message}</ToastBody>
        <Button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          aria-label="Close"
          onclick={() => removeMessage(id)}
        />
      </div>
    </Toast>
  {/each}
</div>
