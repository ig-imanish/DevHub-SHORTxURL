# URL Shortener API Documentation

## Base URL
`/api/v1`

## Endpoints

### 1. Test Endpoint
- **Method:** GET
- **Endpoint:** `/hello-world`
- **Description:** A simple test endpoint to check if the service is running.
- **Response:** 
  - Status: 200 OK
  - Body: "Hello World"

### 2. Get All Shortened URLs
- **Method:** GET
- **Endpoint:** `/shorten`
- **Description:** Retrieves a list of all shortened URLs.
- **Response:**
  - Status: 200 OK
  - Body: List of all shortened URLs.

### 3. Get Shortened URLs by User
- **Method:** GET
- **Endpoint:** `/shorten/user/{username}`
- **Description:** Retrieves all shortened URLs created by a specific user.
- **Path Parameters:** 
  - `username` (String): The username of the user.
- **Response:**
  - Status: 200 OK
  - Body: List of shortened URLs created by the user.

### 4. Get Shortened URLs for Authenticated User
- **Method:** GET
- **Endpoint:** `/shorten/user`
- **Description:** Retrieves all shortened URLs created by the authenticated user.
- **Response:**
  - Status: 200 OK
  - Body: List of shortened URLs created by the authenticated user.

### 5. Get URL by Short URL
- **Method:** GET
- **Endpoint:** `/shorten/{shortUrl}`
- **Description:** Retrieves the original URL associated with a specific shortened URL.
- **Path Parameters:** 
  - `shortUrl` (String): The short URL.
- **Response:**
  - Status: 200 OK
  - Body: Original URL and associated details.
  - Status: 404 Not Found
  - Body: Error message if the short URL is not found.

### 6. Shorten a URL
- **Method:** POST
- **Endpoint:** `/shorten`
- **Description:** Shortens a given original URL.
- **Request Body:**
  **Request Body:**
```json
{
  "originalUrl": "https://example.com",
  "customName": "1234",
  "expirationTime" : "Thu Feb 20 15:56:38 IST 2025"
}
```
**Response:**
```json
{
    "success": true,
    "id": "67b702131f9248416c9487a5",
    "originalUrl": "https://example.com",
    "shortUrl": "123",
    "username": "manish",
    "createdAt": "2025-02-20T10:21:07.620+00:00",
    "expirationTime": "2025-02-20T10:26:38.000+00:00"
}
```

**Error Response:**
```json
{
    "success": false,
    "message": "Custom name already taken",
    "timestamp": "2025-02-20T10:21:25.918+00:00"
}
```
- **Response:**
  - Status: 200 OK
  - Body: Details of the shortened URL.
  - Status: 400 Bad Request
  - Body: Error message if the request is invalid.
  - Status: 401 Unauthorized
  - Body: Error message if the user is not authenticated.

### 7. Delete a Shortened URL
- **Method:** DELETE
- **Endpoint:** `/delete/{shortUrl}`
- **Description:** Deletes a specific shortened URL.
- **Path Parameters:** 
  - `shortUrl` (String): The short URL to be deleted.
- **Response:**
  - Status: 204 No Content
  - Status: 400 Bad Request
  - Body: Error message if the request is invalid.
  - Status: 401 Unauthorized
  - Body: Error message if the user is not authenticated.
  - Status: 404 Not Found
  - Body: Error message if the short URL is not found.

### 8. Get Analytics for a Short URL
- **Method:** GET
- **Endpoint:** `/shorten/{shortUrl}/analytics`
- **Description:** Retrieves analytics for a specific shortened URL.
- **Path Parameters:** 
  - `shortUrl` (String): The short URL.
- **Response:**
  - Status: 200 OK
  - Body: Analytics data for the short URL.
  - Status: 404 Not Found
  - Body: Error message if the short URL is not found.

## Models

### 1. ShortenUrlRequest
```json
{
  "originalUrl": "String",
  "customName": "String",
  "expirationTime": "String"
}
```

### 2. ShortenUrlResponse
```json
{
  "success": true,
  "id": "String",
  "originalUrl": "String",
  "shortUrl": "String",
  "username": "String",
  "createdAt": "Date",
  "expirationTime": "Date"
}
```

### 3. ShortenUrlError
```json
{
  "success": false,
  "message": "String",
  "timestamp": "Date"
}
```

## Error Handling
All error responses include a message and a timestamp.

## Authorization
Some endpoints require user authentication. If the user is not authenticated, a 401 Unauthorized status is returned.
