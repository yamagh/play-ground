import type { PagedResult } from "@/utils/api";

export type User = {
  id: number;
  name: string;
  email: string;
};

export async function findUsers(q: string, page: number, perPage: number): Promise<PagedResult<User> | null> {
  try {
    const params = new URLSearchParams({
      page: page.toString(),
      perPage: perPage.toString(),
    });
    if (q) {
      params.append("q", q);
    }
    const response = await fetch(`/api/users?${params.toString()}`);
    if (response.ok) {
      return await response.json();
    } else {
      console.error("Failed to fetch users:", response.statusText);
      return null;
    }
  } catch (error) {
    console.error("Error fetching users:", error);
    return null;
  }
}
