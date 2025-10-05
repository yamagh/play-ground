<script lang="ts">
  import {
    Button,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader,
    Input,
    Table,
    Spinner,
  } from "@sveltestrap/sveltestrap";
  import PaginationWithTotal from '@/components/common/PaginationWithTotal.svelte';
  import { findUsers, type User } from "./api";

  let {
    isOpen = $bindable(false),
    selectedUsers = $bindable([])
  } = $props<{
    isOpen: boolean,
    selectedUsers: User[] }
  >();
  let q = $state("");
  let users = $state<User[]>([]);
  let isLoading = $state(false);
  let page = $state(1);
  let perPage = 10;
  let total = $state(0);
  let selectedUserMap = $state(new Map<number, User>());

  $effect(() => {
    if (isOpen) {
      const map = new Map<number, User>();
      for (const user of selectedUsers) {
        map.set(user.id, user);
      }
      selectedUserMap = map;
    }
  });

  async function loadUsers() {
    isLoading = true;
    const result = await findUsers(q, page, perPage);
    if (result) {
      users = result.items;
      total = result.total;
    }
    isLoading = false;
  }

  function search() {
    page = 1;
    loadUsers();
  }

  function handlePageChange(e: CustomEvent<number>) {
    page = e.detail;
    loadUsers();
  }

  function handleSelect() {
    selectedUsers = Array.from(selectedUserMap.values());
    isOpen = false;
  }

  function handleCheckboxChange(e: Event, user: User) {
    const target = e.target as HTMLInputElement;
    if (target.checked) {
      selectedUserMap.set(user.id, user);
    } else {
      selectedUserMap.delete(user.id);
    }
    selectedUserMap = new Map(JSON.parse(JSON.stringify(Array.from(selectedUserMap))));
  }
</script>

<Modal {isOpen} toggle={() => isOpen = !isOpen} size="lg">
  <ModalHeader toggle={() => isOpen = !isOpen}>Select Users</ModalHeader>
  <ModalBody>
    <div class="d-flex gap-2 mb-3">
      <Input bind:value={q} placeholder="Search by name or email" on:keydown={(e) => e.key === 'Enter' && search()} />
      <Button color="primary" on:click={search} disabled={isLoading}>
        {#if isLoading}
          <Spinner size="sm" />
        {:else}
          Search
        {/if}
      </Button>
    </div>
    <Table hover bordered striped>
      <thead>
        <tr>
          <th></th>
          <th>Name</th>
          <th>Email</th>
        </tr>
      </thead>
      <tbody>
        {#each users as user}
          <tr>
            <td>
              <input
                type="checkbox"
                checked={selectedUserMap.has(user.id)}
                onchange={(e) => handleCheckboxChange(e, user)}
              />
            </td>
            <td>{user.name}</td>
            <td>{user.email}</td>
          </tr>
        {/each}
      </tbody>
    </Table>
    <PaginationWithTotal {page} {total} {perPage} onchange={handlePageChange} />
  </ModalBody>
  <ModalFooter>
    <Button color="primary" onclick={handleSelect}>OK</Button>
    <Button color="secondary" onclick={() => isOpen = false}>Cancel</Button>
  </ModalFooter>
</Modal>
