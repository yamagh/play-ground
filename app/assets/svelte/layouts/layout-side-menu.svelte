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
    sidebarOpened = true,
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
    },
  ]
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
<div class="vh-100 d-flex pt-5">
  <div class="d-flex flex-column flex-shrink-0 bg-light border-end sidebar {sidebarOpened ? "sidebar-opened" : "sidebar-closed"} {sidebarOpened ? "p-3" : ''}">
    <ul class="nav nav-pills flex-column mb-auto {sidebarOpened ? "" : "nav-flush text-center"}">
      {#each menuItemList as item }
        <li class="nav-item">
          <a
            href="{item.href}"
            class="nav-link {item.label == activeMenu ? "active" : "link-dark"} {sidebarOpened ? "" : "py-3 border-bottom"}"
            data-bs-toggle={sidebarOpened ? "" : "tooltip"}
            data-bs-placement={sidebarOpened ? "" : "right"}
            data-bs-title={sidebarOpened ? "" : item.label}
            aria-current="page"
          >
            <Icon name={item.icon}/>
            {#if sidebarOpened}
              <span class="ms-2">{item.label}</span>
            {/if}
          </a>
        </li>
      {/each}
    </ul>
  </div>
  <div class="flex-grow-1 p-3 main-content">
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
