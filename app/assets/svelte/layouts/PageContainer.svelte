<script lang="ts">
  import { Container, Icon } from "@sveltestrap/sveltestrap";
  import MessageContainer from "../components/layouts/MessageContainer.svelte";

  let {
    title,
    breadcrumbs = ()=>{},
    actions = ()=>{},
    footer = ()=>{},
    children = ()=>{},
    fluid = false,
    backHref = "",
  } = $props<{
    title?: string;
    breadcrumbs?: () => void;
    actions?: () => void;
    footer?: () => void;
    children?: () => void;
    fluid?: boolean;
    backHref?: string;
  }>();
</script>

<svelte:head>
  <title>{title}</title>
</svelte:head>

<div class="position-relative page d-flex flex-column justify-content-between">
  <Container fluid={fluid} class="mt-4">
    {#if breadcrumbs}
      <div class="mb-2">
        {@render breadcrumbs()}
      </div>
    {/if}
    {#if title || actions}
      <div class="d-flex justify-content-between align-items-center mb-4">
        <div class="fs-1 d-flex align-items-center">
          {#if backHref}
            <div class="back-button me-2 text-secondary">
              <a href={backHref} ><Icon name="arrow-left-short" /></a>
            </div>
          {/if}
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

<MessageContainer />

<style>
  .page {
    min-height: calc(100vh - 47px);
  }
  footer {
    width: 100%;
  }
  .back-button:hover {
    cursor: pointer;
    background-color: rgba(0, 0, 0, 0.1);
    border-radius: 50%;
  }
</style>
