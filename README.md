# 🔐 Spring Security – JWT Role & Permission Based Authentication

A secure backend authentication and authorization system built using **Spring Boot** and **Spring Security**.

This project demonstrates an enterprise-style security architecture with JWT-based authentication and role-permission based authorization.

---

## 🚀 Features

- JWT-based stateless authentication
- Role-Based Access Control (RBAC)
- Permission-based fine-grained authorization
- Method-level security using `@PreAuthorize`
- Custom `UserDetailsService` implementation
- BCrypt password encryption
- Secure token validation using custom JWT filter
- Stateless session management

---

## 🏗️ Architecture

The project follows a hierarchical authorization model:

- A **User** can have one or multiple roles  
- A **Role** contains multiple permissions  
- Permissions define granular access control  

---

## 🔑 Security Implementation

- Authentication using **JWT (JSON Web Token)**
- Authorization using:
  - `hasRole()`
  - `hasPermission()`
  - `@PreAuthorize`
- Custom Security configuration using `SecurityFilterChain`
- Password encryption using **BCrypt**

---

## 🛠️ Tech Stack

- Java  
- Spring Boot  
- Spring Security  
- JWT  
- Gradle  
- Postman (API Testing)
