<script lang="ts">
  import {
    Icon,
    Dropdown,
    DropdownItem,
    DropdownMenu,
    DropdownToggle,
  } from '@sveltestrap/sveltestrap';
  import { onMount } from 'svelte';

  let userName = '';

  onMount(async () => {
    try {
      const response = await fetch('/api/me');
      if (response.ok) {
        const user = await response.json();
        userName = user.name;
      } else {
        console.error('Failed to fetch user info');
      }
    } catch (error) {
      console.error('Error fetching user info:', error);
    }
  });
</script>

<div class="fs-6">
  <Dropdown setActiveFromChild>
    <DropdownToggle nav class="nav-link d-flex align-items-center">
      <Icon name="person-circle" />
      {#if userName}
        <span class="ms-2">{userName}</span>
      {/if}
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
</div>
