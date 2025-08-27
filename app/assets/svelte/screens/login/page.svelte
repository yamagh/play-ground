<script lang="ts">
</script>

<div class="vh-100 d-flex justify-content-center align-items-center">
  <div class="mx-auto card shadow p-5" style:width=25rem>
    <div class="mb-5 text-center">
      <i class="bi bi-person-fill-lock text-gray-300" style:font-size=10rem></i>
    </div>
    <div class="form-floating mb-3">
      <input type="email" class="form-control" id="floatingInput" placeholder="name@example.com">
      <label for="floatingInput">Email address</label>
    </div>
    <div class="form-floating">
      <input type="password" class="form-control" id="floatingPassword" placeholder="Password">
      <label for="floatingPassword">Password</label>
    </div>
    <div class="mt-5 d-grid">
      <button type="submit" class="btn btn-primary">Login</button>
    </div>
  </div>
</div>


<style>
  :global(body) {
    background-color: var(--bs-tertiary-color);
  }
</style>
