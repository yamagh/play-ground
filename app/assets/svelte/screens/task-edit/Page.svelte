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
  import { createTask, findTask, updateTask, type Task } from "../task/api";
  import { toast } from "@/stores/toast";

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

  async function handleSave() {
    try {
      if (id) {
        await updateTask(id, task);
      } else {
        await createTask(task);
      }
      toast.set({
        type: "success",
        message: "Task saved successfully!",
      });
      window.location.href = "/tasks";
    } catch (e) {
      toast.set({
        type: "danger",
        message: "Failed to save task.",
      });
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
            <Form onSubmit={handleSave}>
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
                <Button color="secondary" href="/tasks" tag="a">Cancel</Button>
                <Button color="primary" type="submit" class="ms-2">Save</Button>
              </div>
            </Form>
          </CardBody>
        </Card>
      </Col>
    </Row>
  </PageContainer>
</LayoutSideMenu>
