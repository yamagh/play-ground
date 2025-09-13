declare module '@sveltestrap/sveltestrap'

declare module '@/layouts' {
  import { SvelteComponent } from 'svelte'
  export class LayoutSideMenu extends SvelteComponent {}
  export class LayoutSideMenu2 extends SvelteComponent {}
  export class LayoutSideMenu3 extends SvelteComponent {}
  export class PageContainer extends SvelteComponent {}
}
