<script lang="ts">
  import { Button, ButtonGroup, Card, CardBody, Col, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, FormGroup, Icon, Input, InputGroup, InputGroupText, Popover, Row, Tooltip } from "@sveltestrap/sveltestrap";
  import LayoutSideMenu from '@/layouts/LayoutSideMenu.svelte';
  import PageContainer from '@/layouts/PageContainer.svelte';
  import SearchBoxWithFilters from '@/components/SearchBoxWithFilters/SearchBoxWithFilters.svelte';
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
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
    Main text<br>
  </PageContainer>
</LayoutSideMenu>

<style>
</style>
