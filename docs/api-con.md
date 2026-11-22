# Library API

**URL:** `http://localhost:8080/library/api/`

| Method | Path | Success | Errors | Description |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/books` | `200 OK` (list) | — | Get all books |
| `GET` | `/api/books/{id}` | `200 OK` | `404 Not Found` | Get a book by ID |
| `POST` | `/api/books` | `201 Created` + `Location` | `400 Bad Request` | Create a new book |
| `PUT` | `/api/books/{id}` | `200 OK` | `400 Bad Request`, `404 Not Found` | Update an existing book |
| `DELETE` | `/api/books/{id}` | `204 No Content` | `404 Not Found` | Delete a book |
| `GET` | `/api/members` | `200 OK` | — | Get all members |
| `POST` | `/api/members` | `201 Created` | `400 Bad Request` | Register a new member |
| `POST` | `/api/loans` | `201 Created` | `400 Bad Request`, `404 Not Found`, `409 Conflict` | Create a new loan |
| `POST` | `/api/loans/{id}/return` | `200 OK` | `404 Not Found`, `409 Conflict` | Return a loaned book |

### Book
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "available": true
}
```

### Member
```json
{
  "id": 1,
  "name": "Ahmed",
  "email": "ahmed@example.com"
}
```

### Loan
```json
{
  "id": 1,
  "bookId": 1,
  "memberId": 1,
  "loanedAt": "2026-11-19T10:00:00Z",
  "returnedAt": null
}
```
