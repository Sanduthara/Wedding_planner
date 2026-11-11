# My Bookings

This is the customer My Bookings component, with customer sign-in/registration and the booking list/cancel API it needs. Other application screens and APIs are intentionally omitted.

The booking queries use the existing `weddingplanner` MySQL schema, including its event, payment, package, and vendor tables.

## Run

1. Make sure MySQL is running and `src/main/resources/application-local.properties` points to a populated `weddingplanner` schema. This local file is Git-ignored; create it with your own database credentials if it is not present.
2. From this folder, start the backend:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

3. In a second terminal, start the frontend:

   ```powershell
   Set-Location .\frontend
   npm ci
   npm run dev
   ```

4. Open the Vite URL, sign in as a customer, and go to My Bookings.
