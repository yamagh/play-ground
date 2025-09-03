# Gemini.md

This file provides instructions for AI coding agents working on this repository. It is based on the [AGENTS.md](https://agents.md) format.

## Project Overview

This project is a web application built with the Play Framework (using Scala and Java) for the backend and Svelte for the frontend. It appears to be a task management application.

## How to get started

### Prerequisites

- Java Development Kit (JDK)
- sbt (the Scala Build Tool)
- Node.js and npm

### Build and Run

1.  **Install frontend dependencies:**
    ```bash
    npm install
    ```
2.  **Run the application (backend and frontend):**
    ```bash
    sbt run
    ```

The application will be available at `http://localhost:9000`. The Play Framework handles the backend, and `webpack` (configured in `webpack.config.js`) compiles and serves the Svelte frontend assets.

## Testing

To run the tests, use the following command:

```bash
sbt test
```

Browser tests are located in the `test/browsers/` directory.

## Code Style

- **Backend (Scala/Java):** Follow standard Play Framework conventions.
- **Frontend (Svelte/TypeScript):** This project uses TypeScript (`tsconfig.json`) and Tailwind CSS (`tailwind.config.js`). Please adhere to the existing coding styles.

## Key File Locations

- **Backend Controllers:** `app/controllers/`
- **API Controllers:** `app/controllers/api/`
- **Backend Models:** `app/models/`
- **Database Evolutions:** `conf/evolutions/default/`
- **Routes:** `conf/routes`
- **Svelte Components:** `app/assets/svelte/`
- **Main Svelte Screens:** `app/assets/svelte/screens/`

## Security Considerations

- Be mindful of common web application vulnerabilities (e.g., XSS, CSRF).
- Sanitize user input, especially in controllers.
- Use parameterized queries to prevent SQL injection (the project uses Ebean ORM, which helps with this).

## Commit Messages

Please follow a conventional commit message format to maintain a clear and understandable project history.
