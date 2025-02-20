
---

# **API Documentation**

## **Authentication API**
```http
GET /api/v1/auth/{username}
```
Returns `true` if the user is authenticated, otherwise `false`.

## **Shorten URL**
```http
POST /api/v1/shorten
```
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


## **Redirect to Original URL**
```http
GET /api/v1/{shortUrl}
```

## **Delete Short URL**
```http
DELETE /api/v1/delete/{shortUrl}
```

---