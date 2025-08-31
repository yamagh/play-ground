<script lang="ts">
  import {
    Button,
    ButtonGroup,
    Col,
    Icon,
    Input,
    Pagination,
    PaginationItem,
    PaginationLink,
    Row,
    Table,
  } from "@sveltestrap/sveltestrap";
  import LayoutSideMenu from '@/layouts/LayoutSideMenu.svelte';
  import PageContainer from '@/layouts/PageContainer.svelte';
  import SearchBoxWithFilters from '@/components/SearchBoxWithFilters/SearchBoxWithFilters.svelte';
  import { onMount } from "svelte";
  import { findTasks, type Task } from "@/utils/api";

  let tasks: Task[] = [];
  let total = 0;
  let page = 1;
  const perPage = 10;

  onMount(async () => {
    await loadTasks();
  });

  async function loadTasks() {
    const result = await findTasks(page, perPage);
    if (result) {
      tasks = result.items;
      total = result.total;
    }
  }

  function handlePageChange(newPage: number) {
    page = newPage;
    loadTasks();
  }
</script>

<LayoutSideMenu activeMenu="Tasks">
  <PageContainer title="Tasks" fluid={false}>
    {#snippet actions()}
      <div class="hstack gap-3">
        <SearchBoxWithFilters>
          <Row class="mb-3">
            <Col xs=3><label for="task-title" class="col-form-label">Title</label></Col>
            <Col><Input id="task-title" /></Col>
          </Row>
          <Row class="mb-3">
            <Col xs=3><label for="task-description" class="col-form-label">Description</label></Col>
            <Col><Input id="task-description" /></Col>
          </Row>
          <Row>
            <Col xs=3><label for="status" class="col-sm-3 col-form-label">Status</label></Col>
            <Col>
              <ButtonGroup id="status" size="sm">
                <input type="checkbox" class="btn-check" id="btn-check-1" autocomplete="off">
                <label class="btn btn-outline-primary" for="btn-check-1">Open</label>
                <input type="checkbox" class="btn-check" id="btn-check-2" autocomplete="off">
                <label class="btn btn-outline-primary" for="btn-check-2">Working</label>
                <input type="checkbox" class="btn-check" id="btn-check-3" autocomplete="off">
                <label class="btn btn-outline-primary" for="btn-check-3">Done</label>
              </ButtonGroup>
            </Col>
          </Row>
        </SearchBoxWithFilters>
        <Button color="primary" class="text-nowrap"><Icon name="plus-circle-fill" class="me-1"/> Add New Task</Button>
      </div>
    {/snippet}
    <Table hover>
      <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Description</th>
        <th>Status</th>
      </tr>
      </thead>
      <tbody>
      {#each tasks as task}
        <tr>
          <td>{task.id}</td>
          <td>{task.title}</td>
          <td>{task.description}</td>
          <td>{task.status}</td>
        </tr>
      {/each}
      </tbody>
    </Table>

    <Pagination aria-label="Page navigation">
      <PaginationItem disabled={page <= 1}>
        <PaginationLink previous href="#" on:click={() => handlePageChange(page - 1)} />
      </PaginationItem>
      {#each Array(Math.ceil(total / perPage)) as _, i}
        <PaginationItem active={page === i + 1}>
          <PaginationLink href="#" on:click={() => handlePageChange(i + 1)}>
            {i + 1}
          </PaginationLink>
        </PaginationItem>
      {/each}
      <PaginationItem disabled={page >= Math.ceil(total / perPage)}>
        <PaginationLink next href="#" on:click={() => handlePageChange(page + 1)} />
      </PaginationItem>
    </Pagination>
  </PageContainer>
</LayoutSideMenu>

<style>
</style>