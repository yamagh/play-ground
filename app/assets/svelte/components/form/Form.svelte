<script lang="ts">
  let {
    action = "/",
    children = () => {},
    onSubmit
  } = $props<{
    action?: string,
    children?: any,
    onSubmit?: (form: HTMLFormElement) => Promise<any>
  }>()

  const csrf = document.getElementById("app")?.attributes.csrf.value

  const submit = async (form: HTMLFormElement) => {
    const formData = new FormData(form);
    const body = new URLSearchParams();
    for (const pair of formData.entries()) {
        body.append(pair[0], pair[1] as string);
    }

    const res = await fetch(action, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: body,
    })
    return res.json()
  }

  const handleFormSubmit = (event: SubmitEvent) => {
    event.preventDefault();
    const form = event.target as HTMLFormElement;
    if (onSubmit) {
      onSubmit(form)
    } else {
      submit(form).then(data => {
        if (data.success && data.redirectUrl) {
          window.location.href = data.redirectUrl;
        }
      })
    }
  }

  export { submit }
</script>

<form onsubmit={handleFormSubmit}>
  <input type="hidden" name="csrfToken" value={csrf} />
  {@render children()}
</form>

<style>
  form {
    margin-block-end: 0;
  }
</style>
