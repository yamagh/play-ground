<script lang="ts">
  import {
    Alert,
    Button,
    Form,
    FormGroup,
    Input,
    Label,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader
  } from "@sveltestrap/sveltestrap";
  import { postForm } from "@/utils/api";
  import { message as messageStore } from "@/stores/message";

  let {
    isOpen = $bindable(),
    url,
    successMessage,
    onImported,
    modalTitle = "CSV Import",
  } = $props<{ 
    isOpen: boolean,
    url: string,
    successMessage: (response: any) => string,
    onImported?: () => void,
    modalTitle?: string,
  }>();

  let fileInput: HTMLInputElement;
  let selectedFile = $state<File | null>(null);
  let errors = $state<string[]>([]);
  let isLoading = $state(false);

  function handleFileSelect(e: Event) {
    const target = e.target as HTMLInputElement;
    if (target.files && target.files.length > 0) {
      selectedFile = target.files[0];
      errors = [];
    }
  }

  async function handleImport() {
    if (!selectedFile) {
      return;
    }
    isLoading = true;
    errors = [];

    const formData = new FormData();
    formData.append("csv", selectedFile);

    try {
      const result = await postForm<{ count: number }>(url, formData);
      if (result) {
        messageStore.success(successMessage(result));
        if (onImported) {
          onImported();
        }
        isOpen = false;
      }
    } catch (e: any) {
      try {
        const errorPayload = JSON.parse(e.message);
        if (errorPayload.errors) {
          errors = errorPayload.errors;
        } else {
          errors = [e.message || "An unexpected error occurred."];
        }
      } catch (parseError) {
        errors = [e.message || "An unexpected error occurred."];
      }
    } finally {
      isLoading = false;
    }
  }

  $effect(() => {
    // Reset state when modal is closed
    if (!isOpen) {
      selectedFile = null;
      errors = [];
      if (fileInput) {
        fileInput.value = "";
      }
    }
  });
</script>

<Modal bind:isOpen>
  <ModalHeader>{modalTitle}</ModalHeader>
  <ModalBody>
    {#if errors.length > 0}
      <Alert color="danger">
        <p>The following errors were found in the CSV file:</p>
        <ul>
          {#each errors as error}
            <li>{error}</li>
          {/each}
        </ul>
      </Alert>
    {/if}
    <Form>
      <FormGroup>
        <Label for="csv-file">CSV File</Label>
        <Input type="file" id="csv-file" accept=".csv" bind:this={fileInput} on:change={handleFileSelect} />
      </FormGroup>
    </Form>
  </ModalBody>
  <ModalFooter>
    <Button color="secondary" on:click={() => isOpen = false}>Cancel</Button>
    <Button color="primary" on:click={handleImport} disabled={!selectedFile || isLoading}>
      {#if isLoading}
        <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        Importing...
      {:else}
        Import
      {/if}
    </Button>
  </ModalFooter>
</Modal>
