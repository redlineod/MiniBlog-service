# MiniBlog-service

## Overview
MiniBlog-service is a full-featured blogging platform built with Spring Boot.  
It allows users to create, manage, and categorize blog posts, supporting authentication, role-based access, and RESTful API endpoints for all core operations.  
The app is designed for extensibility and easy integration with modern frontends.

---

## Features
- User registration and authentication (JWT-based)  
- Create, update, delete, and view blog posts  
- Categorize posts and assign tags  
- Role-based access control (admin/user)  
- Post status management (draft, published)  
- Reading time calculation for posts  
- RESTful API for all operations  
- Pagination and filtering for posts  
- Dockerized for easy deployment  

---

## Tech Stack
- Java 21  
- Spring Boot 3.5.5  
- Spring Data JPA  
- Spring Security  
- Lombok  
- MapStruct  
- PostgreSQL (default DB)  
- Docker & Docker Compose  

---

## Available REST API Endpoints

### Auth
- `POST /api/v1/auth` — Login, returns JWT token  

### Users
- `GET /api/v1/users/{id}` — Get user by ID  
- `POST /api/v1/users` — Register new user  

### Posts
- `GET /api/v1/posts` — List posts (supports filtering by category/tag)  
- `GET /api/v1/posts/{postId}` — Get post by ID  
- `POST /api/v1/posts` — Create new post  
- `PUT /api/v1/posts/{postId}` — Update post  
- `DELETE /api/v1/posts/{postId}` — Delete post  

### Categories
- `GET /api/v1/categories` — List categories  
- `POST /api/v1/categories` — Create category  

### Tags
- `GET /api/v1/tags` — List tags  
- `POST /api/v1/tags` — Create tag  

---

## Data Model

### User
| Field      | Type           | Description          |
|------------|---------------|----------------------|
| id         | UUID          | Unique identifier    |
| email      | String        | User email           |
| password   | String        | User password        |
| name       | String        | User name            |
| posts      | List<Post>    | User's blog posts    |
| createdAt  | LocalDateTime | Date of creation     |

### Post
| Field      | Type           | Description                         |
|------------|---------------|-------------------------------------|
| id         | UUID          | Unique identifier                   |
| title      | String        | Post title                          |
| content    | String        | Post content                        |
| status     | Enum          | Post status (DRAFT, PUBLISHED)      |
| author     | User          | Reference to the post's author      |
| category   | Category      | Reference to the related category   |
| tags       | Set<Tag>      | Tags assigned to the post           |
| readingTime| int           | Estimated reading time (minutes)    |
| createdAt  | LocalDateTime | Date of creation                    |

### Category
| Field      | Type        | Description                        |
|------------|------------|------------------------------------|
| id         | UUID       | Unique identifier                  |
| name       | String     | Category name                      |
| posts      | List<Post> | Posts under this category           |

### Tag
| Field      | Type        | Description                        |
|------------|------------|------------------------------------|
| id         | UUID       | Unique identifier                  |
| name       | String     | Tag name                           |
| posts      | Set<Post>  | Posts associated with this tag      |
