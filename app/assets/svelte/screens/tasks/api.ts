import { get, post, put, del } from "@/utils/api";
import type { PagedResult } from "@/utils/api";
import { safeRun } from "@/utils/safeRun";

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
  ownerIds?: number[];
}

export const findTasks = safeRun(async (page: number, perPage: number, condition: SearchCondition = {}): Promise<Page<Task> | null> => {
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
  if (condition.ownerIds && condition.ownerIds.length > 0) {
    condition.ownerIds.forEach(id => params.append("ownerIds", id.toString()));
  }
  return await get<PagedResult<Task>>(`/api/tasks?${params.toString()}`);
});

export const findTask = safeRun(async (id: number): Promise<Task | null> => {
  return await get<Task>(`/api/tasks/${id}`);
});

export const createTask = safeRun(async (task: Partial<Task>): Promise<Task | null> => {
  return await post<Task>("/api/tasks", task);
});

export const updateTask = safeRun(async (id: number, task: Partial<Task>): Promise<Task | null> => {
  return await put<Task>(`/api/tasks/${id}`, task);
});

export const deleteTask = safeRun(async (id: number): Promise<boolean> => {
  return (await del(`/api/tasks/${id}`)) !== null;
});
