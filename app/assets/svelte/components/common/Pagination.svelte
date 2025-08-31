<script lang="ts">
  import {
    Pagination,
    PaginationItem,
    PaginationLink,
  } from "@sveltestrap/sveltestrap";
  import { createEventDispatcher } from "svelte";

  export let page: number;
  export let total: number;
  export let perPage: number;

  const dispatch = createEventDispatcher();

  function handlePageChange(e: MouseEvent, newPage: number) {
    e.preventDefault();
    dispatch('change', newPage);
  }
</script>

<Pagination aria-label="Page navigation">
  <PaginationItem disabled={page <= 1}>
    <PaginationLink previous href="#" on:click={(e) => handlePageChange(e, page - 1)} />
  </PaginationItem>
  {#each Array(Math.ceil(total / perPage)) as _, i}
    <PaginationItem active={page === i + 1}>
      <PaginationLink href="#" on:click={(e) => handlePageChange(e, i + 1)}>
        {i + 1}
      </PaginationLink>
    </PaginationItem>
  {/each}
  <PaginationItem disabled={page >= Math.ceil(total / perPage)}>
    <PaginationLink next href="#" on:click={(e) => handlePageChange(e, page + 1)} />
  </PaginationItem>
</Pagination>
