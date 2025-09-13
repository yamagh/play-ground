declare module '@sveltestrap/sveltestrap'

declare module '@/components/common/Pagination.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class Pagination extends SvelteComponent {}
}

declare module '@/components/common/PaginationWithTotal.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class PaginationWithTotal extends SvelteComponent {}
}

declare module '@/components/form/Form.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class Form extends SvelteComponent {}
}

declare module '@/components/layouts/AppTitle.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class AppTitle extends SvelteComponent {}
}

declare module '@/components/layouts/MessageContainer.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class MessageContainer extends SvelteComponent {}
}

declare module '@/components/layouts/UserInfo.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class UserInfo extends SvelteComponent {}
}

declare module '@/components/layouts/VerticalMenu.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class VerticalMenu extends SvelteComponent {}
}

declare module '@/components/layouts/VerticalMenu2.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class VerticalMenu2 extends SvelteComponent {}
}

declare module '@/components/SearchBoxWithFilters/SearchBoxWithFilters.svelte' {
  import { SvelteComponent } from 'svelte'
  export default class SearchBoxWithFilters extends SvelteComponent {}
}
