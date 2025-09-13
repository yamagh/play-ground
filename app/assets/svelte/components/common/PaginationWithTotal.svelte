<script lang="ts">
  import Pagination from '@/components/common/Pagination.svelte';

  const { page, total, perPage, onchange } = $props<{
    page: number;
    total: number;
    perPage: number;
    onchange?: (e: CustomEvent<number>) => void;
  }>();
</script>

<div class="d-flex justify-content-between mt-3">
  <div>
    {#if total > 0}
      <small>Total: {total} records</small>
    {/if}
  </div>
  <div>
    <Pagination {page} {total} {perPage} on:change={onchange} />
  </div>
</div>
