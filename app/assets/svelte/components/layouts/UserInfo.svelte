<script lang="ts">
  import {
    Icon,
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
  } from '@sveltestrap/sveltestrap';
</script>

<Dropdown setActiveFromChild>
  <DropdownToggle nav class="nav-link">
    <Icon name="person-circle" />
  </DropdownToggle>
  <DropdownMenu>
    <DropdownItem href="#">
      <Icon name="gear" class="me-2" />
      Settings
    </DropdownItem>
    <hr class="my-2">
    <DropdownItem href="#">
      <Icon name="box-arrow-right" class="me-2" />
      Logout
    </DropdownItem>
  </DropdownMenu>
</Dropdown>
