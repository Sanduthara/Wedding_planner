# Vendor Portal (Standalone)

This folder is a standalone copy of the vendor portal UI and the backend/API code it depends on. It is separate from the integrated project.

## Requirements

- Java 21
- MySQL 8
- Node.js and npm

## Database setup

Create the empty standalone database schema from the project root:

```powershell
cmd /c "mysql -u root -p < database\schema.sql"
```

The schema creates the required tables and ten wedding-service categories for package creation; it does not copy users, credentials, or wedding-planner records. The application connects to `weddingplanner_vendor` by default. Override the connection with `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables as needed.

## Run

Open two terminals in this folder.

Backend:

```powershell
$env:DB_PASSWORD = "your-local-mysql-password"
$env:APP_AUTH_TOKEN_SECRET = "use-a-random-secret-at-least-32-characters-long"
.\mvnw.cmd spring-boot:run
```

Frontend:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5174/vendor/register.html` to apply as a vendor, or `http://localhost:5174/login.html` to sign in. The frontend calls the backend at `http://localhost:8082/api`. These ports are separate from the integrated project's usual frontend/backend ports.

Vendor accounts require admin approval before package management is enabled. This vendor-only UI does not include administrator screens; approval is handled by the backend's admin API and requires an administrator account.
