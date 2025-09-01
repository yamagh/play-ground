<script lang="ts">
  import { Form } from "@/components";
  import { Alert, Button, Card, CardBody, FormGroup, Icon, Input } from "@sveltestrap/sveltestrap";

  let email = "admin@invalid.com";
  let password = "FFFFFF";
  let formComponent: Form;
  let errorMessage = "";

  const handleLogin = async (form: HTMLFormElement) => {
    const result = await formComponent.submit(form);
    if (result.success) {
      window.location.href = result.redirectUrl;
    } else {
      errorMessage = "Invalid email or password.";
    }
  }
</script>

<div class="vh-100 d-flex justify-content-center align-items-center">
  <div class="mx-auto" style:width=25rem>
    <Card class="shadow p-3">
      <CardBody>
        <div class="mb-5 text-center" style:font-size=10rem>
          <Icon name="rocket" class=text-gray-300/>
        </div>
        {#if errorMessage}
          <Alert color="danger">{errorMessage}</Alert>
        {/if}
        <Form action="/login" onSubmit={handleLogin} bind:this={formComponent}>
          <FormGroup floating label="Email">
            <Input name="email" type="email" placeholder="name@example.com" bind:value={email} autocomplete="on" />
          </FormGroup>
          <FormGroup floating label="Password" spacing=pb-4>
            <Input name="password" type="password" placeholder="Password" bind:value={password} />
          </FormGroup>
          <Button type="submit" color=primary block>Login</Button>
        </Form>
        </CardBody>
      </Card>
  </div>
</div>

<style>
  :global(body) {
    background-color: var(--bs-light);
  }
</style>
