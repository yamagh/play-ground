<script lang="ts">
  import { Button, Toast, ToastBody } from '@sveltestrap/sveltestrap';
  import { toast } from '../../stores/toast';
  import type { Toast as ToastType } from '../../stores/toast';

  let toasts: ToastType[] = [];

  toast.subscribe((value) => {
    toasts = value;
  });

  const removeToast = (id: number) => {
    toast.remove(id);
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
  class="top-0 start-50 translate-middle-x position-fixed m-3"
  style="z-index: 1100"
>
  {#each toasts as { id, message, type } (id)}
    {@const color = colorMap[type] || 'primary'}
    <Toast isOpen={true} {color} class="text-bg-{color} mb-2">
      <div class="d-flex">
        <ToastBody>{message}</ToastBody>
        <Button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          aria-label="Close"
          onclick={() => removeToast(id)}
        />
      </div>
    </Toast>
  {/each}
</div>
