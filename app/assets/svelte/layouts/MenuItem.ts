type MenuItem = {
  label: string;
  icon?: string;
  image?: string;
  href: string;
  isAppTitle?: boolean;
}

export const menuItemList: MenuItem[] = [
  {
    label: "Sample App",
    image: "/assets/images/logo1.svg",
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
