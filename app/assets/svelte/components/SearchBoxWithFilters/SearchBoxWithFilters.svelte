<script lang="ts">
  import { Button, ButtonGroup, Card, CardBody, Col, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, FormGroup, Icon, Input, InputGroup, InputGroupText, Popover, Row, Tooltip } from "@sveltestrap/sveltestrap";

  let {
    value = $bindable(),
    href = "#",
    children = ()=>{},
    searchTooltip = "Search",
    filterTooltip = "Advanced search options",
    searchLabel = "Search",
    width = 480,
  } = $props();

</script>

<div style:width={width}px>
  <InputGroup>
    <Button color="primary" id="search">
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
</div>

<style>
.dropdown-toggle::after {
  content: none;
}
</style>
