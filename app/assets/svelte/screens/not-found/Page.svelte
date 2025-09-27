<script lang="ts">
  import { Button, Col, Row } from "@sveltestrap/sveltestrap";
  import LayoutSideMenu3 from "@/layouts/LayoutSideMenu3.svelte";
  import PageContainer from "@/layouts/PageContainer.svelte";
</script>

<LayoutSideMenu3>
  <PageContainer title="Page Not Found" showMessage={false}>
    <Row>
      <Col class="text-center">
        <h1 class="display-1">404</h1>
        <p class="lead">The page you are looking for does not exist.</p>
        <p>It might have been moved or deleted.</p>
        <Button color="primary" href="/">Go to Home</Button>
      </Col>
    </Row>
  </PageContainer>
</LayoutSideMenu3>
