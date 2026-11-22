import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class LibraryApiTest {

    static final String BASE = System.getProperty("api.base", "http://localhost:8080/library");
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private HttpResponse<String> send(String method, String path, String json ) throws Exception {

        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(BASE + path));

        if (json != null) {

            builder.header("Content-Type", "application/json");

            builder.method(method, HttpRequest.BodyPublishers.ofString(json));

        } else if (method.equals("POST")) {

            builder.POST(HttpRequest.BodyPublishers.noBody());

        } else if (method.equals("DELETE")) {

            builder.DELETE();

        } else {

            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        return CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }


    private long createBook() throws Exception {

        var response = send(
                "POST",
                "/api/books",
                """
                {
                    "title": "Test Book",
                    "author": "Test Author",
                    "isbn": "9780375760396"
                }
                """
        );

        assertEquals(201, response.statusCode());
        return idFromLocation(response);

    }


    private long createMember() throws Exception {

        var response = send(
                "POST",
                "/api/members",
                """
                {
                    "name": "Test Member",
                    "email": "test@example.com"
                }
                """
        );

        assertEquals(201, response.statusCode());
        return idFromLocation(response);

    }


    private long createLoan(long bookId, long memberId) throws Exception {

        var response = send(
                "POST",
                "/api/loans",
                """
                {
                    "bookId": %d,
                    "memberId": %d
                }
                """.formatted(bookId, memberId)
        );

        assertEquals(201, response.statusCode());
        return idFromLocation(response);

    }


    private long idFromLocation(HttpResponse<String> response) {

        String location = response.headers().firstValue("Location").orElseThrow();
        String path = URI.create(location).getPath();
        return Long.parseLong(path.substring(path.lastIndexOf('/') + 1));

    }

    @Test
    void createValidBookReturns201WithLocation() throws Exception {

        var response = send(
                "POST",
                "/api/books",
                """
                {
                    "title": "Clean Code",
                    "author": "Robert C. Martin",
                    "isbn": "9780132350884"
                }
                """
        );

        assertEquals(201, response.statusCode());
        assertTrue(response.headers().firstValue("Location").isPresent());

    }

    @Test
    void blankTitleReturns400() throws Exception {

        var response = send(
                "POST",
                "/api/books",
                """
                {
                    "title": "",
                    "author": "Author",
                    "isbn": "9780375760396"
                }
                """
        );

        assertEquals(400, response.statusCode());
        assertTrue(response.headers().firstValue("Content-Type").orElse("").contains("application/problem+json"));
    }

    @Test
    void unknownBookReturns404() throws Exception {

        var response = send("GET", "/api/books/999999", null);
        assertEquals(404, response.statusCode());

    }

    @Test
    void deleteBookReturns204() throws Exception {

        long bookId = createBook();
        var response = send("DELETE", "/api/books/" + bookId, null);
        assertEquals(204, response.statusCode());

    }


    @Test
    void borrowAvailableBookReturns201() throws Exception {

        long bookId = createBook();
        long memberId = createMember();

        var response = send(
                "POST",
                "/api/loans",
                """
                {
                    "bookId": %d,
                    "memberId": %d
                }
                """.formatted(bookId, memberId)
        );

        assertEquals(201, response.statusCode());

    }

    @Test
    void borrowSameBookTwiceReturns409() throws Exception {

        long bookId = createBook();
        long memberId = createMember();

        var first = send(
                "POST",
                "/api/loans",
                """
                {
                    "bookId": %d,
                    "memberId": %d
                }
                """.formatted(bookId, memberId));

        assertEquals(201, first.statusCode());

        var second = send(
                "POST",
                "/api/loans",
                """
                {
                    "bookId": %d,
                    "memberId": %d
                }
                """.formatted(bookId, memberId));

        assertEquals(409, second.statusCode());

    }

    @Test
    void fourthLoanForMemberReturns409() throws Exception {

        long memberId = createMember();
        long book1 = createBook();
        long book2 = createBook();
        long book3 = createBook();
        long book4 = createBook();

        assertEquals(
                201,
                send(
                        "POST",
                        "/api/loans",
                        """
                        {
                            "bookId": %d,
                            "memberId": %d
                        }
                        """.formatted(book1, memberId)
                ).statusCode());


        assertEquals(
                201,
                send(
                        "POST",
                        "/api/loans",
                        """
                        {
                            "bookId": %d,
                            "memberId": %d
                        }
                        """.formatted(book2, memberId)
                ).statusCode());


        assertEquals(
                201,
                send(
                        "POST",
                        "/api/loans",
                        """
                        {
                            "bookId": %d,
                            "memberId": %d
                        }
                        """.formatted(book3, memberId)
                ).statusCode());

        var fourth = send(
                "POST",
                "/api/loans",
                """
                {
                    "bookId": %d,
                    "memberId": %d
                }
                """.formatted(book4, memberId));

        assertEquals(409, fourth.statusCode());

    }

    @Test
    void returnLoanReturns200() throws Exception {

        long bookId = createBook();
        long memberId = createMember();
        long loanId = createLoan(bookId, memberId);

        var response = send("POST", "/api/loans/" + loanId + "/return", null);
        assertEquals(200, response.statusCode());

    }

}