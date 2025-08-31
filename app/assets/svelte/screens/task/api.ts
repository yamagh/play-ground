import { fetchJson } from "@/utils/api";
import type { PagedResult } from "@/utils/api";

export type Task = {
  id: number;
  title: string;
  status: string;
  ownerName: string;
  dueDate: string;
  priority: number;
  createdOn: string;
}

export type Page<T> = PagedResult<T>;

export interface SearchCondition {
  title?: string;
  statuses?: string[];
}

export async function findTasks(page: number, perPage: number, condition: SearchCondition = {}): Promise<Page<Task> | null> {
  const params = new URLSearchParams({
    page: page.toString(),
    perPage: perPage.toString(),
  });
  if (condition.title) {
    params.append("title", condition.title);
  }
  if (condition.statuses && condition.statuses.length > 0) {
    condition.statuses.forEach(s => params.append("statuses", s));
  }
  return await fetchJson<PagedResult<Task>>(`/api/tasks?${params.toString()}`);
}

