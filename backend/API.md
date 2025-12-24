# Expense Tracker Backend API Documentation

This document provides a comprehensive overview of the RESTful APIs exposed by the Expense Tracker backend service. All endpoints are designed to be clean, predictable, and frontend-friendly.

## Authentication

Most endpoints require authentication via a JWT Bearer token in the `Authorization` header. This token must be obtained through the `/auth/login` or `/auth/register` endpoints.

### Getting a Token

1.  **Register:** Use `POST /auth/register` to create a new user account. A JWT token will be returned upon successful registration.
2.  **Login:** Use `POST /auth/login` with your credentials. A valid JWT token will be returned if authentication is successful.

### Endpoints: Authentication

#### `POST /auth/register`

Registers a new user and returns a JWT token.

**Request:**

```
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "securepassword123",
  "currency": "USD",
  "monthlyBudget": 1500.00 // Optional
}
```

**Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Possible Error Codes:**

*   `400 Bad Request`: If email already exists or invalid input.

#### `POST /auth/login`

Authenticates a user and returns a JWT token.

**Request:**

```
POST /auth/login
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "password": "securepassword123"
}
```

**Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Possible Error Codes:**

*   `401 Unauthorized`: Invalid credentials.

#### `GET /auth/me`

Returns the email of the authenticated user.

**Request:**

```
GET /auth/me
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (200 OK):**

```json
"john.doe@example.com"
```

**Possible Error Codes:**

*   `401 Unauthorized`: No token provided or invalid/expired token.

## Endpoints: User

#### `GET /users/profile`

Retrieves the profile of the authenticated user.

**Request:**

```
GET /users/profile
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (200 OK):**

```json
{
  "id": "user123",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "currency": "USD",
  "monthlyBudget": 1500.00,
  "createdAt": "2023-10-27T10:00:00Z"
}
```

**Possible Error Codes:**

*   `401 Unauthorized`: No token provided or invalid/expired token.
*   `404 Not Found`: User profile not found (should ideally not happen if authenticated).

#### `PUT /users/profile`

Updates the profile of the authenticated user.

**Request:**

```
PUT /users/profile
Authorization: Bearer <YOUR_JWT_TOKEN>
Content-Type: application/json

{
  "name": "Johnathan Doe",
  "currency": "INR",
  "monthlyBudget": 2000.00
}
```

**Response (200 OK):**

```json
{
  "id": "user123",
  "name": "Johnathan Doe",
  "email": "john.doe@example.com",
  "currency": "INR",
  "monthlyBudget": 2000.00,
  "createdAt": "2023-10-27T10:00:00Z"
}
```

**Possible Error Codes:**

*   `400 Bad Request`: Invalid input (e.g., negative budget).
*   `401 Unauthorized`: No token provided or invalid/expired token.

## Endpoints: Categories

#### `GET /categories`

Retrieves all categories, including user-specific and default categories.

**Request:**

```
GET /categories
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (200 OK):**

```json
[
  {
    "id": "cat1",
    "userId": null,
    "name": "Food",
    "type": "EXPENSE",
    "createdAt": "2023-01-01T00:00:00Z"
  },
  {
    "id": "cat2",
    "userId": "user123",
    "name": "Salary",
    "type": "INCOME",
    "createdAt": "2023-10-28T11:00:00Z"
  }
]
```

**Possible Error Codes:**

*   `401 Unauthorized`: No token provided or invalid/expired token.

#### `POST /categories`

Creates a new user-specific category.

**Request:**

```
POST /categories
Authorization: Bearer <YOUR_JWT_TOKEN>
Content-Type: application/json

{
  "name": "Groceries",
  "type": "EXPENSE"
}
```

**Response (201 Created):**

```json
{
  "id": "newCat456",
  "userId": "user123",
  "name": "Groceries",
  "type": "EXPENSE",
  "createdAt": "2023-10-28T12:00:00Z"
}
```

**Possible Error Codes:**

*   `400 Bad Request`: Category with this name already exists for the user or as a default.
*   `401 Unauthorized`: No token provided or invalid/expired token.

#### `DELETE /categories/{id}`

Deletes a user-specific category.

**Request:**

```
DELETE /categories/newCat456
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (204 No Content):**

```
(No content)
```

**Possible Error Codes:**

*   `400 Bad Request`: Default categories cannot be deleted, or category is used by existing expenses.
*   `401 Unauthorized`: No token provided or invalid/expired token.
*   `404 Not Found`: Category not found or unauthorized access.

## Endpoints: Expenses

#### `GET /expenses`

Retrieves expenses for the authenticated user, with optional filtering.

**Request (all optional query parameters):**

```
GET /expenses?month=10&year=2023&categoryId=cat1&type=EXPENSE
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (200 OK):**

```json
[
  {
    "id": "exp1",
    "userId": "user123",
    "amount": 50.00,
    "categoryId": "cat1",
    "type": "EXPENSE",
    "paymentMethod": "CARD",
    "date": "2023-10-20",
    "note": "Weekly groceries",
    "createdAt": "2023-10-20T15:00:00Z"
  }
]
```

**Possible Error Codes:**

*   `401 Unauthorized`: No token provided or invalid/expired token.
*   `400 Bad Request`: Invalid month/year format if provided.

#### `GET /expenses/{id}`

Retrieves a single expense by ID for the authenticated user.

**Request:**

```
GET /expenses/exp1
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (200 OK):**

```json
{
  "id": "exp1",
  "userId": "user123",
  "amount": 50.00,
  "categoryId": "cat1",
  "type": "EXPENSE",
  "paymentMethod": "CARD",
  "date": "2023-10-20",
  "note": "Weekly groceries",
  "createdAt": "2023-10-20T15:00:00Z"
}
```

**Possible Error Codes:**

*   `401 Unauthorized`: No token provided or invalid/expired token.
*   `404 Not Found`: Expense not found or unauthorized access.

#### `POST /expenses`

Creates a new expense for the authenticated user.

**Request:**

```
POST /expenses
Authorization: Bearer <YOUR_JWT_TOKEN>
Content-Type: application/json

{
  "amount": 75.50,
  "categoryId": "cat3",
  "type": "EXPENSE",
  "paymentMethod": "UPI",
  "date": "2023-10-28",
  "note": "Dinner with friends" // Optional
}
```

**Response (201 Created):**

```json
{
  "id": "newExp789",
  "userId": "user123",
  "amount": 75.50,
  "categoryId": "cat3",
  "type": "EXPENSE",
  "paymentMethod": "UPI",
  "date": "2023-10-28",
  "note": "Dinner with friends",
  "createdAt": "2023-10-28T18:30:00Z"
}
```

**Possible Error Codes:**

*   `400 Bad Request`: Amount must be greater than zero, expense date cannot be in the future, category not found or unauthorized access.
*   `401 Unauthorized`: No token provided or invalid/expired token.

#### `PUT /expenses/{id}`

Updates an existing expense for the authenticated user.

**Request:**

```
PUT /expenses/newExp789
Authorization: Bearer <YOUR_JWT_TOKEN>
Content-Type: application/json

{
  "amount": 80.00,
  "categoryId": "cat3",
  "type": "EXPENSE",
  "paymentMethod": "CARD",
  "date": "2023-10-28",
  "note": "Updated dinner expense"
}
```

**Response (200 OK):**

```json
{
  "id": "newExp789",
  "userId": "user123",
  "amount": 80.00,
  "categoryId": "cat3",
  "type": "EXPENSE",
  "paymentMethod": "CARD",
  "date": "2023-10-28",
  "note": "Updated dinner expense",
  "createdAt": "2023-10-28T18:30:00Z"
}
```

**Possible Error Codes:**

*   `400 Bad Request`: Amount must be greater than zero, expense date cannot be in the future, category not found or unauthorized access.
*   `401 Unauthorized`: No token provided or invalid/expired token.
*   `404 Not Found`: Expense not found or unauthorized access.

#### `DELETE /expenses/{id}`

Deletes an expense for the authenticated user.

**Request:**

```
DELETE /expenses/newExp789
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (204 No Content):**

```
(No content)
```

**Possible Error Codes:**

*   `401 Unauthorized`: No token provided or invalid/expired token.
*   `404 Not Found`: Expense not found or unauthorized access.

#### `GET /expenses/summary/monthly`

Retrieves a monthly summary of income and expenses for the authenticated user.

**Request:**

```
GET /expenses/summary/monthly?year=2023&month=10
Authorization: Bearer <YOUR_JWT_TOKEN>
```

**Response (200 OK):**

```json
{
  "year": 2023,
  "month": 10,
  "totalIncome": 2500.00,
  "totalExpense": 1200.50,
  "balance": 1299.50,
  "categoryExpenseSummary": {
    "cat1": 500.00,
    "cat3": 700.50
  },
  "categoryIncomeSummary": {
    "cat2": 2500.00
  }
}
```

**Possible Error Codes:**

*   `400 Bad Request`: Invalid year or month.
*   `401 Unauthorized`: No token provided or invalid/expired token.

## HTTP Status Codes

This API utilizes standard HTTP status codes to indicate the success or failure of an API request. Here are the common codes you can expect:

*   **`200 OK`**: The request was successful.
*   **`201 Created`**: The request was successful and a new resource was created.
*   **`204 No Content`**: The request was successful, but there is no content to return (e.g., successful deletion).
*   **`400 Bad Request`**: The request was malformed or invalid (e.g., missing required fields, invalid data, business rule violation).
*   **`401 Unauthorized`**: Authentication is required, but no valid token was provided or the token is invalid/expired.
*   **`404 Not Found`**: The requested resource could not be found.
*   **`500 Internal Server Error`**: An unexpected error occurred on the server.

## Security

*   **Password Storage:** User passwords are not stored directly but are hashed using BCrypt before being saved to the database. This ensures that even if the database is compromised, passwords remain secure.
*   **JWT Tokens:** JWTs are used for stateless authentication. They are signed with a strong secret key to prevent tampering. Tokens have an expiration time (24 hours by default) after which they become invalid, requiring the user to re-authenticate.
*   **HTTPS in Production:** While development might occur over HTTP, it is **mandatory** to deploy the application with HTTPS in a production environment to ensure all communication between the client and server is encrypted and secure.
*   **Headers:** The necessary `Authorization` header with a `Bearer` token must be manually added by the client for protected routes. Other headers, such as `Content-Type: application/json`, are expected for request bodies.

