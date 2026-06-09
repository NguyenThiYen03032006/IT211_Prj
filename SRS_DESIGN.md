# IT211 Project - SRS Analysis And Backend Design

## Actors And Use Cases

| Actor | Main use cases | Functional mapping |
|---|---|---|
| Admin | Manage users, manage courses, view enrollments, delete users | User API, Course API, Enrollment API |
| Lecturer | Create/update courses, upload materials, view submissions, grade submissions | Course API, Material API, Submission API, Grade API |
| Student | Register/login, enroll course, upload submission, view materials and grades | Auth API, Enrollment API, Submission API, Material API, Grade API |

## Database Summary

| Table | Purpose | Main constraints |
|---|---|---|
| users | Store account/profile data | unique email, unique studentCode |
| roles | Store ADMIN/LECTURER/STUDENT | unique name |
| user_roles | Many-to-many user-role mapping | FK user_id, role_id |
| courses | Course/class section | unique code, FK lecturer_id |
| enrollments | Student joins course | unique student_id + course_id |
| submissions | Student uploaded work | FK student_id, course_id |
| grades | Lecturer evaluates submission | unique submission_id |
| materials | Lecturer uploaded learning files | FK course_id, uploaded_by |
| refresh_tokens | Long-lived token rotation | unique token, revoked flag |
| token_blacklist | Invalidated access tokens | unique token |

## Text ERD

```text
Role >---< User
User(LECTURER) 1---< Course
User(STUDENT) 1---< Enrollment >---1 Course
User(STUDENT) 1---< Submission >---1 Course
Submission 1---1 Grade >---1 User(LECTURER)
Course 1---< Material >---1 User(LECTURER)
User 1---< RefreshToken
TokenBlacklist stores invalidated JWT strings
```

## API And Authorization Matrix

| API group | Examples | ADMIN | LECTURER | STUDENT | PUBLIC |
|---|---|---:|---:|---:|---:|
| Auth | POST /api/v1/auth/login, /register, /refresh | Yes | Yes | Yes | Yes |
| Users | /api/v1/users | Yes | No | No | No |
| Courses | GET /api/v1/courses | Yes | Yes | Yes | No |
| Courses write | POST/PUT/DELETE /api/v1/courses | Yes | Yes | No | No |
| Enrollments | /api/v1/enrollments | Yes | No | Yes | No |
| Submissions | /api/v1/submissions | Yes | Yes | Yes | No |
| Grades | /api/v1/grades | Yes | Yes | Yes(read) | No |
| Materials | GET /api/v1/materials/courses/{id} | Yes | Yes | Yes | No |
| Materials upload | POST /api/v1/materials/upload | Yes | Yes | No | No |

## Security Flow

1. Login: client posts email/password, Spring Security verifies BCrypt password, backend returns access token and refresh token.
2. Access token: client sends `Authorization: Bearer <token>`, `JwtAuthenticationFilter` validates signature/expiry/blacklist, then loads roles.
3. Refresh token: backend checks token exists, not revoked and not expired, then issues a new access token.
4. Logout: backend stores current access token in `token_blacklist` and marks refresh token revoked.
5. Blacklist: every request rejects a JWT found in `token_blacklist`.

## AOP Logging

`LoggingAspect` logs login, logout, upload file, grade submission, create course, delete user, plus service exceptions with `@AfterThrowing`.

## Roadmap

1. Database and entity model.
2. Repository layer.
3. Security, JWT, refresh token, blacklist.
4. Auth API.
5. User and course management.
6. Enrollment, submission, grade, material APIs.
7. Cloudinary/S3 implementation.
8. AOP logging.
9. Validation, exception handling, testing.
