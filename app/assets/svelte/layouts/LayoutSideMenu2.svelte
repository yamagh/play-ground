<script lang="ts">
  import { Icon } from '@sveltestrap/sveltestrap';
  import { UserInfo, AppTitle, VerticalMenu } from '@/components';

  let {
    activeMenu = "Home",
    collapsibleMenu = true,
    sidebarOpened = true,
    children = ()=>{}
  } = $props()
</script>

<div class="vh-100 d-flex">
  <VerticalMenu {activeMenu} {sidebarOpened} showAppTitle={true} />
  <div class="flex-grow-1 main-content">
    <div class="w-100 shadow-sm border-bottom p-2 d-flex justify-content-between sticky-top bg-white align-items-center fs-5">
      <div class="d-flex">
        {#if collapsibleMenu}
          <span class="ms-3">
            <Icon name="layout-sidebar" onclick={()=>{sidebarOpened = !sidebarOpened}} role="button"/>
            <div class="vr ms-3"></div>
          </span>
        {/if}
      </div>
      <div class="me-5">
        <UserInfo />
      </div>
    </div>
    <div class="mt-5">
      {@render children()}
    </div>
  </div>
</div>

<style>
.main-content {
  overflow: auto;
}
</style>
