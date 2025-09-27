<script lang="ts">
  import { Icon } from '@sveltestrap/sveltestrap';
  import { menuItemList } from '@/layouts/MenuItem';
  
    let {
      showAppTitle = false,
      activeHref,
    } = $props<{
      showAppTitle?: boolean;
      activeHref?: string;
    }>()

  const currentPath = activeHref || window.location.pathname;
</script>

<div class="bg-white border-end sidebar px-3">
  <ul class="nav nav-pills flex-column mb-auto">
    {#each menuItemList as item }
      {#if showAppTitle || !item.isAppTitle}
        {#if item.children && item.children.length > 0}
          <!-- Menu with children -->
          <li class="nav-item">
            <a
              href="#{item.label}-collapse"
              data-bs-toggle="collapse"
              class="nav-link {item.children.some(c => c.href === currentPath) ? '' : 'link-dark'}"
            >
              <div class="d-flex align-items-center">
                {#if item.image}
                  <img src={item.image} alt={item.label} class="app-logo" />
                {:else if item.icon}
                  <Icon name={item.icon}/>
                {/if}
                <span class="ms-3">{item.label}</span>
              </div>
            </a>
          </li>
          <ul
            id="{item.label}-collapse"
            class="collapse {item.children.some(c => c.href === currentPath) ? 'show' : ''} flex-column ms-3 sub-menu"
            style:padding-left=0
            style:list-style=none
          >
            {#each item.children as child}
              <li class="nav-item">
                <a
                  href="{child.href}"
                  class="nav-link {currentPath === child.href ? 'active' : 'link-dark'}"
                >
                  {#if child.icon}
                    <Icon name={child.icon}/>
                  {/if}
                  <span class="ms-3">{child.label}</span>
                </a>
              </li>
            {/each}
          </ul>
        {:else}
          <!-- Menu without children -->
          <li class="nav-item">
            <a
              id="{item.label}-link"
              href="{item.href}"
              class="
                nav-link
                {!item.isAppTitle && currentPath === item.href ? "active" : "link-dark"}
                {item.isAppTitle ? "fs-4 my-3" : ""}
                "
              aria-current="page"
            >
              <div class="d-flex align-items-center">
                {#if item.image}
                  <img src={item.image} alt={item.label} class="app-logo" />
                {:else if item.icon}
                  <Icon name={item.icon}/>
                {/if}
                <span class="ms-3">{item.label}</span>
              </div>
            </a>
          </li>
        {/if}
      {/if}
    {/each}
  </ul>
</div>

<style>
.app-logo {
  width: 28px;
  height: 28px;
  vertical-align: text-bottom;
}
.sidebar {
  overflow: auto;
  width: 300px;
}
.sub-menu {
  visibility: visible;
  flex-direction: column;
}
.sub-menu li {
  width: 249px;
}
</style>
