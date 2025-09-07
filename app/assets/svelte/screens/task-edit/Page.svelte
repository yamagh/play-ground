<script lang="ts">
  import {
    Button,
    Card,
    CardBody,
    CardHeader,
    Col,
    Form,
    FormGroup,
    Input,
    Label,
    Row,
  } from "@sveltestrap/sveltestrap";
  import LayoutSideMenu from "@/layouts/LayoutSideMenu.svelte";
  import PageContainer from "@/layouts/PageContainer.svelte";
  import { onMount } from "svelte";
  import { createTask, findTask, updateTask, deleteTask, type Task } from "../tasks/api";
  import { message } from "@/stores/message";

  let task: Partial<Task> = {};
  let id: number | null = null;

  onMount(async () => {
    const path = window.location.pathname;
    const match = path.match(/tasks\/(\d+)\/edit/);
    if (match) {
      id = parseInt(match[1], 10);
      const result = await findTask(id);
      if (result) {
        task = result;
      }
    }
  });

  async function handleSave(event: SubmitEvent) {
    event.preventDefault();
    let result;
    if (id) {
      result = await updateTask(id, task);
    } else {
      result = await createTask(task);
    }
    if (result) {
      message.success("Task saved successfully!");
      window.location.href = "/tasks";
    }
  }

  async function handleDelete(event: Event) {
    event.preventDefault();
    if (!id) return;
    if (window.confirm("Are you sure you want to delete this task?")) {
      const result = await deleteTask(id);
      if (result) {
        message.success("Task deleted successfully!");
        window.location.href = "/tasks";
      }
    }
  }
</script>

<LayoutSideMenu activeMenu="Tasks">
  <PageContainer title={id ? 'Edit Task' : 'New Task'}>
    <Row>
      <Col md={{ size: 8, offset: 2 }}>
        <Card>
          <CardHeader>{id ? `Edit Task #${id}` : 'Create New Task'}</CardHeader>
          <CardBody>
            <Form onsubmit={handleSave}>
              <FormGroup>
                <Label for="title">Title</Label>
                <Input id="title" type="text" bind:value={task.title} required />
              </FormGroup>
              <FormGroup>
                <Label for="status">Status</Label>
                <Input id="status" type="select" bind:value={task.status}>
                  <option>Open</option>
                  <option>WIP</option>
                  <option>Done</option>
                </Input>
              </FormGroup>
              <FormGroup>
                <Label for="owner">Owner</Label>
                <Input id="owner" type="select" bind:value={task.ownerId}>
                  <!-- TODO: Load users from API -->
                  <option value={1}>User 1</option>
                  <option value={2}>User 2</option>
                </Input>
              </FormGroup>
              <FormGroup>
                <Label for="dueDate">Due Date</Label>
                <Input id="dueDate" type="date" bind:value={task.dueDate} />
              </FormGroup>
              <FormGroup>
                <Label for="priority">Priority</Label>
                <Input id="priority" type="select" bind:value={task.priority}>
                  <option value={1}>Low</option>
                  <option value={2}>Medium</option>
                  <option value={3}>High</option>
                </Input>
              </FormGroup>
              <div class="text-end">
                <Button color="secondary" href="/tasks">Cancel</Button>
                {#if id}
                  <Button color="danger" class="ms-2" onclick={handleDelete}>Delete</Button>
                {/if}
                <Button color="primary" type="submit" class="ms-2">Save</Button>
              </div>
            </Form>
          </CardBody>
        </Card>
      </Col>
    </Row>
  </PageContainer>
</LayoutSideMenu>
