# Employee Management API - Authentication Testing Guide

## 🔐 Authentication Flow

### 1. Login to get Bearer Token

**POST** `http://localhost:8080/auth/login`

**Request Body:**
```json
{
  "userId": "admin",
  "password": "G5hnX2GMyFEetC1"
}
```

**Response (Success):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY5MDM3MTQwMCwiZXhwIjoxNjkwMzczMjAwfQ.example_jwt_token",
  "type": "Bearer",
  "expiresIn": "1800",
  "message": "Login successful"
}
```

**Response (Failure):**
```json
{
  "error": "Unauthorized",
  "message": "Invalid credentials"
}
```

### 2. Use Bearer Token for Protected Endpoints

**Headers for all protected endpoints:**
```
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json
```

## 📝 **Test Cases**

### ✅ **Test Case 1: Login with Valid Credentials**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "admin",
    "password": "G5hnX2GMyFEetC1"
  }'
```

### ❌ **Test Case 2: Login with Invalid Credentials**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "admin",
    "password": "wrongpassword"
  }'
```

### ✅ **Test Case 3: Access Protected Endpoint with Valid Token**
```bash
# First get the token from login, then use it:
curl -X GET http://localhost:8080/employees \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### ❌ **Test Case 4: Access Protected Endpoint without Token**
```bash
curl -X GET http://localhost:8080/employees
```

### ❌ **Test Case 5: Access Protected Endpoint with Invalid Token**
```bash
curl -X GET http://localhost:8080/employees \
  -H "Authorization: Bearer invalid_token"
```

### ✅ **Test Case 6: Create Employee with Valid Token**
```bash
curl -X POST http://localhost:8080/employees \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "role": "Developer"
  }'
```

## 🔒 **Security Features Implemented**

- ✅ JWT-based authentication with 30-minute expiry
- ✅ Bearer token authorization for all endpoints except `/auth/login`
- ✅ Hardcoded admin credentials (userId: "admin", password: "G5hnX2GMyFEetC1")
- ✅ HTTP 401 Unauthorized for invalid/missing tokens
- ✅ HTTP 401 Unauthorized for invalid credentials
- ✅ Stateless authentication (no server-side sessions)
- ✅ CORS enabled for frontend integration

## 🛡️ **Error Responses**

### Authentication Errors (HTTP 401):
```json
{
  "error": "Authentication failed",
  "message": "Invalid or missing authentication credentials"
}
```

### Invalid Token:
```json
{
  "error": "Authentication failed",
  "message": "Token expired or invalid"
}
```

### Missing Authorization Header:
```json
{
  "error": "Authentication failed",
  "message": "Authorization header missing"
}
```

## 🕒 **Token Expiry**
- Tokens expire in **30 minutes** (1800 seconds)
- After expiry, you need to login again to get a new token
- No token refresh mechanism (as per requirements)

## 🚀 **Quick Start**

1. Start the application
2. Login to get a token: `POST /auth/login`
3. Use the token in Authorization header: `Bearer <token>`
4. Access any protected endpoint with the token
5. Token expires in 30 minutes - login again when needed
