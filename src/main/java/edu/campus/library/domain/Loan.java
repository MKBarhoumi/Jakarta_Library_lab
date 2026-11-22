package edu.campus.library.domain;
import java.time.Instant;

public record Loan(
        Long id,
        long bookId,
        long memberId,
        Instant loanedAt,
        Instant returnedAt
) {
    public boolean isActive() {
        return returnedAt == null;
    }

}
