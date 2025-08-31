type MenuItem = {
  label: string;
  icon: string;
  href: string;
  isAppTitle?: boolean;
}

export const menuItemList: MenuItem[] = [
  {
    label: "Sample App",
    icon: "rocket-fill",
    href: "/",
    isAppTitle: true,
  },
  {
    label: "Home",
    icon: "house-fill",
    href: "#",
  },
  {
    label: "Dashboard",
    icon: "speedometer2",
    href: "#",
  },
  {
    label: "Orders",
    icon: "table",
    href: "#",
  },
  {
    label: "Products",
    icon: "grid",
    href: "#",
  },
  {
    label: "Customers",
    icon: "person-circle",
    href: "#",
  },
  {
    label: "Tasks",
    icon: "list-task",
    href: "/tasks",
  },
]
