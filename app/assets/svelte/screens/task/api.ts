import { fetchJson } from "@/utils/api";
import type { PagedResult } from "@/utils/api";

export type Task = {
  id: number;
  title: string;
  description: string;
  status: string;
}

export async function findTasks(page: number, perPage: number): Promise<PagedResult<Task> | null> {
  return await fetchJson<PagedResult<Task>>(`/api/tasks?page=${page}&perPage=${perPage}`);
}
