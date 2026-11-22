http://localhost:8080/library/api/

Method  Path                    Success         Errors
GET     /api/books              200 list	    —
GET	    /api/books/{id}	        200	            404
POST	/api/books	            201 + Location	400
PUT	    /api/books/{id}	        200	            400, 404
DELETE	/api/books/{id}	        204	            404
GET	    /api/members	        200	            —
POST	/api/members	        201	            400
POST	/api/loans	            201	            400, 404, 409
POST	/api/loans/{id}/return  200	            404, 409

Book
{
"id": 1,
"title": "Clean Code",
"author": "Robert C. Martin",
"isbn": "9780132350884",
"available": true
}


Member
{
"id": 1,
"name": "Ahmed",
"email": "ahmed@example.com"
}


Loan
{
"id": 1,
"bookId": 1,
"memberId": 1,
"loanedAt": "2026-11-19T10:00:00Z",
"returnedAt": null
}

