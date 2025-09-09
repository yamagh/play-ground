type MenuItem = {
  label: string;
  icon?: string;
  image?: string;
  href: string;
  isAppTitle?: boolean;
  children?: MenuItem[];
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
    children: [
      {
        label: "Overview",
        href: "/",
      },
      {
        label: "Daily",
        href: "/daily",
      },
      {
        label: "Monthly",
        href: "/monthly",
      },
      {
        label: "Yearly",
        href: "/yearly",
      },
    ],
  },
  {
    label: "Dashboard",
    icon: "speedometer2",
    href: "#",
    children: [
      {
        label: "Analytics",
        href: "/dashboard/analytics",
      },
      {
        label: "Reporting",
        href: "/dashboard/reporting",
      },
    ]
  },
  {
    label: "Orders",
    icon: "table",
    href: "#",
    children: [
      {
        label: "New Order",
        href: "/orders/new",
      },
      {
        label: "Pending",
        href: "/orders/pending",
      },
      {
        label: "Completed",
        href: "/orders/completed",
      },
      
    ]
  },
  {
    label: "Products",
    icon: "grid",
    href: "#",
    children: [
      {
        label: "List Products",
        href: "/products",
      },
      {
        label: "Add Product",
        href: "/products/add",
      },
    ]
  },
  {
    label: "Customers",
    icon: "person-circle",
    href: "#",
    children: [
      {
        label: "List Customers",
        href: "/customers",
      },
      {
        label: "Add Customer",
        href: "/customers/add",
      },
    ]
  },
  {
    label: "Tasks",
    icon: "list-task",
    href: "/tasks",
    children: [
      {
        label: "List Tasks",
        href: "/tasks",
      },
      {
        label: "Add Task",
        href: "/tasks/add",
      }
    ]
  },
]
