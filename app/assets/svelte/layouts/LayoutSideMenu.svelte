<script lang="ts">
  import { Icon } from '@sveltestrap/sveltestrap';
  import { UserInfo, AppTitle, VerticalMenu } from '@/components';
  import { menuItemList } from '@/layouts/MenuItem';
  import { setupGlobalErrorHandler } from '@/utils/globalErrorHandler';

  const appTitleItem = menuItemList.find(item => item.isAppTitle);

  let {
    activeMenu = "Home",
    collapsibleMenu = true,
    sidebarOpened = true,
    children = ()=>{}
  } = $props()

  setupGlobalErrorHandler();
</script>

<div class="fixed-top shadow-sm border-bottom p-2 d-flex justify-content-between bg-white align-items-center fs-5">
  <div class="d-flex">
    {#if collapsibleMenu}
      <span class="ms-3">
        <Icon name="layout-sidebar" onclick={()=>{sidebarOpened = !sidebarOpened}} role="button"/>
        <div class="vr ms-3"></div>
      </span>
    {/if}
    <AppTitle item={appTitleItem} />
  </div>
  <div class="me-5">
    <UserInfo />
  </div>
</div>
<div class="vh-100 d-flex pt-5">
  <VerticalMenu {activeMenu} {sidebarOpened} />
  <div class="flex-grow-1 main-content">
    {@render children()}
  </div>
</div>

<style>
.main-content {
  overflow: auto;
}
</style>
