# Checkout Component 3.0

by [Filip Cyboran](https://github.com/cyboranf)

## Overview

This project implements a market checkout component with a clean, RESTful API. It calculates the total price of items in a shopping cart, supports multi-priced discounts, bundling discounts, and receipt generation. User authentication with JWT ensures secure access, and the service is designed for scalability and ease of deployment.

## Running Tests

To run tests in your application, use the following command:

```bash
./mvnw test -Dspring.profiles.active=test
```

### Description:
- The `./mvnw` script ensures Maven Wrapper is used, so you don’t need to have Maven installed globally.
- The `-Dspring.profiles.active=test` flag activates the `test` profile, ensuring that the application uses the appropriate configuration (e.g., H2 database).
- All unit, integration, and acceptance tests will be executed, and results will be displayed in the console.

## Building and Running the Application

### Build the Application

To package the application into a JAR file, run the following command:

```bash
./mvnw clean package
```

- `clean`: Removes any previous build artifacts.
- `package`: Packages the application into a JAR file located in the `target` directory (e.g., `target/checkout-0.0.1-SNAPSHOT.jar`).

### Run the Application

After building the application, you can run the JAR file with the following command:

```bash
java -jar target/checkout-0.0.1-SNAPSHOT.jar
```

### Notes:
- Ensure that your production `application.properties` file is correctly configured for the database and other necessary services.
- To run the application with specific profiles, use the `--spring.profiles.active` flag. For example:

```bash
java -jar target/checkout-0.0.1-SNAPSHOT.jar --spring.profiles.active=production
```

## Swagger Documentation

Follow these steps to explore the API in a more interactive format:

1. Open your web browser and navigate to the URL above to access the Swagger API JSON specification.

2. Copy the JSON content displayed in your browser.

3. Go to the Swagger Editor available at:

   https://editor.swagger.io/

4. Paste the copied JSON content into the editor.

5. When prompted with the question: "Would you like to convert your JSON into YAML?", click **OK**.

This will convert the JSON specification into YAML format, allowing you to explore and validate the API schema using the Swagger Editor's interactive interface.