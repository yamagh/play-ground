<script lang="ts">
  import { Button, Toast, ToastBody } from '@sveltestrap/sveltestrap';
  import { toast, hideToast } from '../../stores/toast.ts';

  let show = false;
  let message = '';
  let color = 'primary';

  toast.subscribe((value) => {
    show = value.show;
    message = value.message;
    color = value.color;
    if (show) {
      setTimeout(() => {
        hideToast();
      }, 5000);
    }
  });
</script>

{#if show}
  <div
    class="top-0 start-50 translate-middle-x position-fixed m-3"
    style="z-index: 1100"
  >
    <Toast isOpen={show} color={color} class="text-bg-{color}">
      <div class="d-flex">
        <ToastBody>{message}</ToastBody>
        <Button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></Button>
      </div>
    </Toast>
  </div>
{/if}
