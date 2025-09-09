<script lang="ts">
  import { Icon } from '@sveltestrap/sveltestrap';
  import { UserInfo, AppTitle, VerticalMenu2 } from '@/components';
  import { setupGlobalErrorHandler } from '@/utils/globalErrorHandler';

  let {
    activeMenu = "Home",
    collapsibleMenu = true,
    sidebarOpened = true,
    children = ()=>{}
  } = $props()

  setupGlobalErrorHandler();
</script>

<div class="vh-100 d-flex">
  <VerticalMenu2 {activeMenu} {sidebarOpened} showAppTitle={true} />
  <div class="flex-grow-1 main-content shadow">
    <div class="w-100 border-bottom p-2 d-flex justify-content-between sticky-top bg-white align-items-center fs-5">
      <div class="d-flex">
      </div>
      <div class="me-5">
        <UserInfo />
      </div>
    </div>
    <div class="bg-light">
      {@render children()}
    </div>
  </div>
</div>

<style>
.main-content {
  overflow: auto;
}
</style>
