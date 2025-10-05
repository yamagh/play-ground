<script lang="ts">
  import { Button } from "@sveltestrap/sveltestrap";
  import UserSearchModal from '@/components/UserSearchModal.svelte';
  import type { User } from '@/components/api';

  let { selectedUsers = $bindable<User[]>([]), id = '' } = $props();
  let isUserSearchModalOpen = $state(false);
</script>

<Button {id} color="secondary" on:click={() => isUserSearchModalOpen = true}>
  {#if selectedUsers.length > 0}
    {selectedUsers.length} users selected
  {:else}
    Select Users
  {/if}
</Button>

<UserSearchModal bind:isOpen={isUserSearchModalOpen} bind:selectedUsers />
