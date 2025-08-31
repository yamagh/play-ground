<script lang="ts">
  import { Button, Dropdown, DropdownMenu, Icon, Input, InputGroup, InputGroupText, Tooltip } from "@sveltestrap/sveltestrap";
  import Form from '@/components/form/Form.svelte';
  import { createEventDispatcher } from 'svelte';

  let {
    value = $bindable(),
    children = ()=>{},
    searchTooltip = "Search",
    filterTooltip = "Advanced search options",
    width = 480,
  } = $props();

  const dispatch = createEventDispatcher();

  function handleSearch() {
    dispatch('search');
    return Promise.resolve(true);
  }

</script>

<div style:width={width}px>
  <Form onSubmit={handleSearch}>
    <InputGroup>
      <Button color="primary" id="search" type="submit">
        <Icon name="search" />
        <Tooltip target="search" placement="top">{searchTooltip}</Tooltip>
      </Button>
      <Input placeholder="Search tasks" aria-describedby="search" bind:value={value}></Input>
      <InputGroupText id="filter">
        <Tooltip target="filter" placement="top">{filterTooltip}</Tooltip>
        <button
          class="dropdown-toggle"
          data-bs-toggle="dropdown"
          aria-expanded="false"
          data-bs-offset="16,20"
          data-bs-auto-close="false">
          <Icon name="sliders2" />
        </button>
        <Dropdown>
          <DropdownMenu class="p-3 shadow">
            <div style:width={width-30}px>
              {@render children()}
            </div>
          </DropdownMenu>
        </Dropdown>
      </InputGroupText>
    </InputGroup>
  </Form>
</div>

<style>
  .dropdown-toggle::after {
    content: none;
  }
</style>

