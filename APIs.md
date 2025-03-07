# **DevHub - URL Shortener** `@BristoHQ`
*A secure, scalable, and feature-rich URL shortener RestAPIs built with Spring Boot and MongoDB.*  

![License](https://img.shields.io/badge/License-MIT-blue.svg) ![Java](https://img.shields.io/badge/Java-23-orange.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)  

## **📌 Overview**  
DevHub's **URL Shortener** allows users to **shorten URLs, track analytics, and secure links with authentication**. It offers **basic URL shortening for free** and advanced features like **custom short URLs, analytics, and password protection**.  

## **🛠️ Tech Stack**  
- **Backend:** Spring Boot 
- **Database:** MongoDB
- **Authentication:** JWT Authentication
- **Hosting:** localhost  

---

## **🚀 Features**  

### **✅ Free Features**  
- Shorten long URLs
- Custom short URLs with limitations (`https://url.devhub.bristohq.me/CustomName-ABCD`)
- Basic click analytics (total clicks)  
- Maximum 15 URLs can be shortened (Have to delete old URLs for shortening further!)
- Expiry dates for short URLs (User can store URLs for a maximum of 15 days)
- API access for URL shortening

### 💎 **Premium Features**
- All `✅ Free features`
- Custom short URLs (`https://url.devhub.bristohq.me/CustomName`)  
- Advanced analytics (geo-location, devices, referrers)  
- Password-protected URLs
- Expiry dates for short URLs (As long as they want, i.e., 1 second to lifetime)
- API access for bulk URL shortening (Maximum 499 URLs per day)

---

## **📌 API Documentation**  

### **1️⃣ Authentication**  
#### **🔹 Add JWT to header for each API**  
Include the JWT token in the `Authorization` header for all API requests.

---

### **2️⃣ URL Shortening APIs**  
#### **🔹 Shorten a URL**  
`POST /api/v1/url/shorten`  
```json
{
  "originalUrl": "https://example.com",
  "customName": "1234",
  "expirationTime": "Thu Feb 20 15:56:38 IST 2025"
}
```
✅ **Response:**  
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

#### **🔹 Retrieve Original URL & Redirect**  
`GET /api/v1/{shortUrl}`  
✅ **Response:**  
- Redirects user to the original URL.  

#### **🔹 Delete a Shortened URL**  
`DELETE /api/v1/url/{shortUrl}`  
✅ **Response:**  
```json
{ "message": "URL deleted successfully" }
```

#### **🔹 Update Expiration Date (Premium Only)**  
`PUT /api/v1/url/{shortUrl}/update-expiry`  
```json
{ "newExpiryDate": "2026-01-01T00:00:00Z" }
```
✅ **Response:**  
```json
{ "message": "Expiration date updated successfully" }
```

---

### **3️⃣ Security & Access Control APIs**  
#### **🔹 Set Password for a URL (Premium Only)**  
`POST /api/v1/url/{shortUrl}/set-password`  
```json
{ "password": "securepass123" }
```
✅ **Response:**  
```json
{ "message": "Password set successfully" }
```

#### **🔹 Access a Password-Protected URL**  
`POST /api/v1/url/{shortUrl}/access`  
```json
{ "password": "securepass123" }
```
✅ **Response:**  
```json
{ "originalUrl": "https://example.com" }
```

#### **🔹 Set Expiry for a Short URL**  
`POST /api/v1/url/{shortUrl}/set-expiry`  
```json
{ "expiryDate": "2025-12-31T23:59:59Z" }
```
✅ **Response:**  
```json
{ "message": "Expiry date set successfully" }
```

---

### **4️⃣ Analytics APIs**  
#### **🔹 Get Click Analytics**  
`GET /api/v1/url/{shortUrl}/analytics`  
✅ **Response:**  
```json
{
  "totalClicks": 150,
  "uniqueVisitors": 120,
  "topCountries": ["India", "USA", "UK"],
  "topReferrers": ["google.com", "twitter.com"],
  "topDevices": ["Mobile", "Desktop"]
}
```

#### **🔹 Live Click Tracking**  
`GET /api/v1/url/{shortUrl}/live-tracking`  
✅ **Response:**  
```json
{ "clicks": 151, "lastClickedAt": "2025-03-01T12:34:56Z" }
```

---

### **5️⃣ Admin APIs**  
#### **🔹 Get All Shortened URLs (Admin Only)**  
`GET /api/v1/admin/urls`  
✅ **Response:**  
```json
[
  { "shortUrl": "xyz123", "longUrl": "https://example.com", "clicks": 150 },
  { "shortUrl": "abc456", "longUrl": "https://google.com", "clicks": 250 }
]
```

#### **🔹 Suspend a URL (Admin Only)**  
`POST /api/v1/admin/url/{shortUrl}/suspend`  
✅ **Response:**  
```json
{ "message": "URL suspended successfully" }
```

#### **🔹 Get Premium Users (Admin Only)**  
`GET /api/v1/admin/premium-users`  
✅ **Response:**  
```json
[
  { "username": "manish123", "subscription": "Premium", "expiry": "2026-01-01" }
]
```

---

## **📌 Free vs. Premium Plan**  

| Feature | Free Users | Premium Users |
|---------|------------|---------------|
| Basic URL Shortening | ✅ Yes | ✅ Yes |
| Custom Short URLs | ❌ No | ✅ Yes |
| Basic Click Analytics | ✅ Yes | ✅ Yes |
| Advanced Analytics | ❌ No | ✅ Yes |
| Password-Protected URLs | ❌ No | ✅ Yes |
| API Access (Bulk URL Shortening) | ❌ No | ✅ Yes |
| Higher Rate Limits | ❌ No | ✅ Yes |

---

## **📌 Contact**
💬 **Have questions? Join our DevHub community:** [Discord](https://discord.gg/devhub)
