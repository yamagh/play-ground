<script lang="ts">
  let {
    action = "/",
    children = ()=>{}
  } = $props()

  const csrf = document.getElementById("app")?.attributes.csrf.value

  const handleSubmit = (event: SubmitEvent) => {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    const formData = new FormData(form);
    const body = new URLSearchParams();
    for (const pair of formData.entries()) {
        body.append(pair[0], pair[1] as string);
    }

    fetch(action, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: body,
    }).then((res) => {
      if (res.redirected) {
        window.location.href = res.url;
      }
    });
  }
</script>

<form onsubmit={handleSubmit}>
  <input type="hidden" name="csrfToken" value={csrf} />
  {@render children()}
</form>
