import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContractTest {

    static final String BASE = System.getProperty("api.base", "http://localhost:8080/library");
    static final HttpClient CLIENT = HttpClient.newHttpClient();

    static HttpResponse<String> send(String method, String path, String json) throws Exception {
        var builder = HttpRequest.newBuilder(URI.create(BASE + path))
                .header("Content-Type", "application/json");
        var body = json == null
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(json);
        return CLIENT.send(builder.method(method, body).build(),
                HttpResponse.BodyHandlers.ofString());
    }

    static long idOf(HttpResponse<String> r) {
        Matcher m = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(r.body());
        assertTrue(m.find(), "no id in: " + r.body());
        return Long.parseLong(m.group(1));
    }

    static long newBook() throws Exception {
        var r = send("POST", "/api/books",
                "{\"title\":\"Dune\",\"author\":\"Frank Herbert\",\"isbn\":\"9780441172719\"}");
        assertEquals(201, r.statusCode());
        return idOf(r);
    }

    static long newMember() throws Exception {
        var r = send("POST", "/api/members", "{\"name\":\"Ada\",\"email\":\"ada@campus.edu\"}");
        assertEquals(201, r.statusCode());
        return idOf(r);
    }

    static HttpResponse<String> borrow(long bookId, long memberId) throws Exception {
        return send("POST", "/api/loans",
                "{\"bookId\":" + bookId + ",\"memberId\":" + memberId + "}");
    }

    @Test
    void createValidBookReturns201WithLocation() throws Exception {
        var r = send("POST", "/api/books",
                "{\"title\":\"The Botany of Desire\",\"author\":\"Michael Pollan\",\"isbn\":\"9780375760396\"}");
        assertEquals(201, r.statusCode());
        assertTrue(r.headers().firstValue("Location").isPresent());
    }

    @Test
    void blankTitleReturns400() throws Exception {
        var r = send("POST", "/api/books",
                "{\"title\":\"\",\"author\":\"X\",\"isbn\":\"9780375760396\"}");
        assertEquals(400, r.statusCode());
        assertTrue(r.headers().firstValue("Content-Type").orElse("").contains("problem+json"));
    }

    @Test
    void unknownBookReturns404() throws Exception {
        var r = send("GET", "/api/books/999999", null);
        assertEquals(404, r.statusCode());
    }

    @Test
    void deleteBookReturns204() throws Exception {
        long id = newBook();
        assertEquals(204, send("DELETE", "/api/books/" + id, null).statusCode());
    }

    @Test
    void borrowAvailableBookReturns201() throws Exception {
        assertEquals(201, borrow(newBook(), newMember()).statusCode());
    }

    @Test
    void borrowSameBookTwiceReturns409() throws Exception {
        long book = newBook();
        long member = newMember();
        assertEquals(201, borrow(book, member).statusCode());
        assertEquals(409, borrow(book, newMember()).statusCode());
    }

    @Test
    void fourthLoanForMemberReturns409() throws Exception {
        long member = newMember();
        for (int i = 0; i < 3; i++) {
            assertEquals(201, borrow(newBook(), member).statusCode());
        }
        assertEquals(409, borrow(newBook(), member).statusCode());
    }

    @Test
    void returnLoanReturns200() throws Exception {
        var loan = borrow(newBook(), newMember());
        assertEquals(201, loan.statusCode());
        long loanId = idOf(loan);
        assertEquals(200, send("POST", "/api/loans/" + loanId + "/return", null).statusCode());
    }
}