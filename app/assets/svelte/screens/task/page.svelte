<script lang="ts">
  import { Button, Card, CardBody, FormGroup, Icon, Input } from "@sveltestrap/sveltestrap";
  import LayoutSideMenu from '@/layouts/LayoutSideMenu.svelte';
  import PageContainer from '@/layouts/PageContainer.svelte';
</script>

<LayoutSideMenu>
  <PageContainer title="Tasks" fluid={false}>
    {#snippet breadcrumbs()}
      foo / bar / baz
    {/snippet}
    {#snippet actions()}
      actions
    {/snippet}
    {#snippet footer()}
      footer
    {/snippet}
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
  </PageContainer>
</LayoutSideMenu>

<style>
</style>
