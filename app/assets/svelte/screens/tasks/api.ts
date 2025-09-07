import { get, post, put, del } from "@/utils/api";
import type { PagedResult } from "@/utils/api";

export type Task = {
  id: number;
  title: string;
  status: string;
  ownerId: number;
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
  return await get<PagedResult<Task>>(`/api/tasks?${params.toString()}`);
}

export async function findTask(id: number): Promise<Task | null> {
  return await get<Task>(`/api/tasks/${id}`);
}

export async function createTask(task: Partial<Task>): Promise<Task | null> {
  return await post<Task>("/api/tasks", task);
}

export async function updateTask(id: number, task: Partial<Task>): Promise<Task | null> {
  return await put<Task>(`/api/tasks/${id}`, task);
}

export async function deleteTask(id: number): Promise<boolean> {
  return (await del(`/api/tasks/${id}`)) !== null;
}
