<script lang="ts">
  import {
    Icon,
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
  } from '@sveltestrap/sveltestrap';

  let {
    activeMenu = "Home",
    collapsibleMenu = true,
    children = ()=>{}
  } = $props()

  const menuItemList = [
    {
      label: "Home",
      icon: "house-fill",
      href: "#",
    },
    {
      label: "Dashboard",
      icon: "speedometer2",
      href: "#",
    },
    {
      label: "Orders",
      icon: "table",
      href: "#",
    },
    {
      label: "Products",
      icon: "grid",
      href: "#",
    },
    {
      label: "Customers",
      icon: "person-circle",
      href: "#",
    }
  ]
</script>

<div class="vw-100 shadow-sm border-bottom p-2 d-flex justify-content-between sticky-top bg-white align-items-center fs-5">
  <div>
    {#if collapsibleMenu}
      <span class="ms-4">
        <Icon name="layout-sidebar" />
        <div class="vr ms-2"></div>
      </span>
    {/if}
    <Icon name="rocket-fill" class="mx-2"/>
    <span>Sample Todo App</span>
  </div>
  <div class="me-5">
    <Dropdown setActiveFromChild>
      <DropdownToggle nav class="nav-link">
        <Icon name="person-circle" />
      </DropdownToggle>
      <DropdownMenu>
        <DropdownItem href="#">
          <Icon name="gear" class="me-2" />
          Settings
        </DropdownItem>
        <hr class="my-2">
        <DropdownItem href="#">
          <Icon name="box-arrow-right" class="me-2" />
          Logout
        </DropdownItem>
      </DropdownMenu>
    </Dropdown>
  </div>
</div>
<div class="vh-100 d-flex">
  <div class="h-100 d-flex flex-column flex-shrink-0 p-3 bg-light" style="width: 280px;">
    <ul class="nav nav-pills flex-column mb-auto">
      {#each menuItemList as item }
        <li class="nav-item">
          <a href="{item.href}" class="nav-link {item.label == activeMenu ? "active" : "link-dark"}" aria-current="page">
            <Icon name={item.icon} class="me-2" />
            {item.label}
          </a>
        </li>
      {/each}
    </ul>
  </div>
  <div class="border-start flex-grow-1 p-3">
    {@render children()}
  </div>
</div>


<style>
</style>
