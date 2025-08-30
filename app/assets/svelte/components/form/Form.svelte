<script lang="ts">
  let {
    action = "/",
    children = ()=>{}
  } = $props()

  const csrf = document.getElementById("app")?.attributes.csrf.value
</script>

<form method="POST" action={action}>
  <input type="hidden" name="csrfToken" value={csrf} />
  {@render children()}
</form>
