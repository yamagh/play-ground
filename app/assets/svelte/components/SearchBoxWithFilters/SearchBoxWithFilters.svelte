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
    isOpen = $bindable(false),
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
        <div class="dropdown-center">
          <Tooltip target="filter" placement="top">{filterTooltip}</Tooltip>
          <button
            class="dropdown-toggle border-0 bg-body-tertiary"
            onclick={() => isOpen = !isOpen}
            aria-expanded={isOpen}
            type="button"
          >
            <Icon name="sliders2"/>
          </button>
          <Dropdown bind:isOpen={isOpen} style="left: {40-width}px; top: 20px;" >
            <DropdownMenu class="p-3 shadow">
              <div style:width={width-30}px>
                {@render children()}
              </div>
            </DropdownMenu>
          </Dropdown>
        </div>
      </InputGroupText>
    </InputGroup>
  </Form>
</div>

<style>
  .dropdown-toggle::after {
    content: none;
  }
</style>

