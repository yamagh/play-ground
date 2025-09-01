<script lang="ts">
  import {
    Button,
    ButtonGroup,
    Col,
    Icon,
    Input,
    Row,
    Table,
  } from "@sveltestrap/sveltestrap";
  import { LayoutSideMenu, LayoutSideMenu2 } from '@/layouts';
  import PageContainer from '@/layouts/PageContainer.svelte';
  import SearchBoxWithFilters from '@/components/SearchBoxWithFilters/SearchBoxWithFilters.svelte';
  import Pagination from '@/components/common/Pagination.svelte';
  import { onMount } from "svelte";
  import { findTasks, type Task } from "./api";

  let title = "";
  let statuses: string[] = [];
  let tasks: Task[] = [];
  let filterIsOpen = false;
  let total = 0;
  let page = 1;
  const perPage = 10;

  onMount(async () => {
    await loadTasks();
  });

  async function loadTasks() {
    const result = await findTasks(page, perPage, { title, statuses });
    if (result) {
      tasks = result.items;
      total = result.total;
    }
  }

  function handlePageChange(e: CustomEvent<number>) {
    page = e.detail;
    loadTasks();
  }

  function handleSearch() {
    filterIsOpen = false;
    page = 1;
    loadTasks();
  }

  function handleClear() {
    title = "";
    statuses = [];
  }
</script>

<LayoutSideMenu2 activeMenu="Tasks">
  <PageContainer title="Tasks" fluid={false}>
    {#snippet actions()}
      <div class="hstack gap-3">
        <SearchBoxWithFilters on:search={handleSearch} bind:value={title} bind:isOpen={filterIsOpen}>
          <Row class="mb-3">
            <Col xs=3><label for="task-title" class="col-form-label">Title</label></Col>
            <Col><Input id="task-title" bind:value={title} /></Col>
          </Row>
          <Row>
            <Col xs=3><label for="status" class="col-sm-3 col-form-label">Status</label></Col>
            <Col>
              <ButtonGroup id="status" size="sm">
                <input type="checkbox" class="btn-check" id="btn-check-1" autocomplete="off" value="Open" bind:group={statuses}>
                <label class="btn btn-outline-primary" for="btn-check-1">Open</label>
                <input type="checkbox" class="btn-check" id="btn-check-2" autocomplete="off" value="WIP" bind:group={statuses}>
                <label class="btn btn-outline-primary" for="btn-check-2">WIP</label>
                <input type="checkbox" class="btn-check" id="btn-check-3" autocomplete="off" value="Done" bind:group={statuses}>
                <label class="btn btn-outline-primary" for="btn-check-3">Done</label>
              </ButtonGroup>
            </Col>
          </Row>
          <div class="mt-3 text-right">
            <Button color="" outline on:click={handleClear} type="button" size="sm">Clear</Button>
            <Button color="primary" id="search" type="submit" size="sm" class="ms-2">
              <Icon name="search" class="me-1"/> Search
            </Button>
          </div>
        </SearchBoxWithFilters>
        <Button color="primary" class="text-nowrap" href="/tasks/new" tag="a">
          <Icon name="plus-circle-fill" class="me-1"/> Add New Task
        </Button>
      </div>
    {/snippet}
    <Table hover>
      <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Status</th>
        <th>Owner</th>
        <th>Due Date</th>
        <th>Priority</th>
        <th>Created On</th>
      </tr>
      </thead>
      <tbody>
      {#each tasks as task}
        <tr>
          <td>{task.id}</td>
          <td><a href="/tasks/{task.id}/edit">{task.title}</a></td>
          <td>{task.status}</td>
          <td>{task.ownerName}</td>
          <td>{task.dueDate}</td>
          <td>{task.priority}</td>
          <td>{task.createdOn}</td>
        </tr>
      {/each}
      </tbody>
    </Table>

    <div class="d-flex justify-content-between mt-3">
      <div>
        {#if total > 0}
          <small>Total: {total} records</small>
        {/if}
      </div>
      <div>
        <Pagination {page} {total} {perPage} on:change={handlePageChange} />
      </div>
    </div>
  </PageContainer>
</LayoutSideMenu2>

<style>
</style>
