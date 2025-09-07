<script lang="ts">
  import { Container } from "@sveltestrap/sveltestrap";
  import MessageContainer from "../components/layouts/MessageContainer.svelte";

  let {
    title,
    breadcrumbs = ()=>{},
    actions = ()=>{},
    footer = ()=>{},
    children = ()=>{},
    fluid = false,
  } = $props();
</script>

<div class="position-relative page d-flex flex-column justify-content-between">
  <Container fluid={fluid} class="mt-4">
    {#if breadcrumbs}
      <div class="mb-2">
        {@render breadcrumbs()}
      </div>
    {/if}
    {#if title || actions}
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="fs-1">
          {title}
        </div>
        <div>
          {@render actions()}
        </div>
      </div>
    {/if}
    {#if children}
      <div>
        {@render children()}
      </div>
    {/if}
  </Container>
  {#if footer}
  <footer>
    <Container fluid={fluid} class="p-3">
      {@render footer()}
    </Container>
  </footer>
  {/if}
</div>

<style>
  .page {
    min-height: calc(100vh - 47px);
  }
  footer {
    width: 100%;
  }
</style>

<MessageContainer />
