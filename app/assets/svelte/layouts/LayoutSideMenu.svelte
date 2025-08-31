<script lang="ts">
  import {
    Icon,
    Tooltip,
  } from '@sveltestrap/sveltestrap';
  import { menuItemList } from '@/layouts/MenuItem';
  import UserInfo from '@/components/layouts/UserInfo.svelte';

  let {
    activeMenu = "Home",
    collapsibleMenu = true,
    sidebarOpened = true,
    children = ()=>{}
  } = $props()
</script>

<div class="vw-100 shadow-sm border-bottom p-2 d-flex justify-content-between sticky-top bg-white align-items-center fs-5 position-fixed">
  <div>
    {#if collapsibleMenu}
      <span class="ms-3">
        <Icon name="layout-sidebar" onclick={()=>{sidebarOpened = !sidebarOpened}} role="button"/>
        <div class="vr ms-3"></div>
      </span>
    {/if}
    <Icon name="rocket-fill" class="ms-3 me-2"/>
    <span>Sample Todo App</span>
  </div>
  <div class="me-5">
    <UserInfo />
  </div>
</div>
<div class="vh-100 d-flex pt-5">
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
  <div class="flex-grow-1 main-content">
    {@render children()}
  </div>
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
.main-content {
  overflow: auto;
}
</style>
