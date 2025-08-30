<script lang="ts">
  import { Button, Card, CardBody, FormGroup, Icon, Input } from "@sveltestrap/sveltestrap";
</script>

<div class="vh-100 d-flex justify-content-center align-items-center">
  <div class="mx-auto" style:width=25rem>
    <Card class="shadow p-3">
      <CardBody>
        <div class="mb-5 text-center" style:font-size=10rem>
          <Icon name="rocket" class=text-gray-300/>
        </div>
        <FormGroup floating label="Email">
          <Input type="email" placeholder="name@example.com" />
        </FormGroup>
        <FormGroup floating label="Password" spacing=pb-4>
          <Input type="password" placeholder="Password" />
        </FormGroup>
        <Button color=primary block>Login</Button>
      </CardBody>
    </Card>
  </div>
</div>


<style>
  :global(body) {
    background-color: var(--bs-light);
  }
</style>
