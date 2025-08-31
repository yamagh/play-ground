<script lang="ts">
  import {
    Icon,
    Tooltip,
  } from '@sveltestrap/sveltestrap';
  import { menuItemList } from '@/layouts/MenuItem';

  let {
    activeMenu = "Home",
    sidebarOpened = true,
  } = $props()
</script>

<div class="d-flex flex-column flex-shrink-0 bg-light border-end sidebar {sidebarOpened ? "sidebar-opened" : "sidebar-closed"} {sidebarOpened ? "p-3" : ''}">
  <ul class="nav nav-pills flex-column mb-auto {sidebarOpened ? "" : "nav-flush text-center"}">
    {#each menuItemList as item }
      <li class="nav-item">
        <a
          id="{item.label}-link"
          href="{item.href}"
          class="nav-link {item.label == activeMenu ? "active" : "link-dark"} {sidebarOpened ? "" : "py-3 border-bottom"}"
          aria-current="page"
        >
          <Icon name={item.icon}/>
          {#if sidebarOpened}
            <span class="ms-2">{item.label}</span>
          {/if}
        </a>
        {#if !sidebarOpened}
          <Tooltip target={`${item.label}-link`} placement="right">{item.label}</Tooltip>
        {/if}
      </li>
    {/each}
  </ul>
</div>

<style>
.sidebar {
  height: calc(100vh - 47px);
  overflow: auto;
}
.sidebar-opened {
  width: 280px;
  transition: 0.1s ease;
}
.sidebar-closed {
  width: 66px;
  transition: 0.1s ease;
  a {
    font-size: 1.4rem;
  }
}
.nav-flush .nav-link {
  border-radius: 0;
}.nav-flush .nav-link {
  border-radius: 0;
}
</style>
