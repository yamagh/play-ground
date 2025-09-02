<script lang="ts">
  import { Icon } from "@sveltestrap/sveltestrap";
  import type { MenuItem } from "@/layouts/MenuItem";

  let { item } : { item: MenuItem } = $props();
</script>

<div class="d-flex align-items-center">
  {#if item.image}
    <img src={item.image} alt={item.label} class="app-logo ms-3 me-2" />
  {:else if item.icon}
    <Icon name={item.icon} class="ms-3 me-2"/>
  {/if}
  <span>{item.label}</span>
</div>

<style>
.app-logo {
  width: 28px;
  height: 28px;
}
</style>
