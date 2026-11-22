package edu.campus.library.service;

import edu.campus.library.domain.Book;
import edu.campus.library.domain.Loan;
import edu.campus.library.domain.Member;
import edu.campus.library.repository.BookRepository;
import edu.campus.library.repository.MemberRepository;
import edu.campus.library.repository.LoanRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.time.Clock;
import java.util.List;

@ApplicationScoped
@Logged
public class LoanService {
    private BookRepository books;
    private MemberRepository members;
    private LoanRepository loans;
    private Clock clock;
    protected LoanService() { }

    @Inject
    public LoanService(BookRepository books, MemberRepository members, LoanRepository loans, Clock clock) {
        this.books = books;
        this.members = members;
        this.loans = loans;
        this.clock = clock;
    }

    public List<Loan> findAll() {
        return loans.findAll();
    }

    public Loan borrow(long bookId, long memberId) {
        Book book = books.find(bookId).orElseThrow(NotFoundException::new);

        if (!book.available()) {
            throw new ConflictException("This book is already borrowed.");
        }

        Member member = members.find(memberId).orElseThrow(NotFoundException::new);

        long activeLoans = loans.countActiveByMemberId(memberId);

        if (activeLoans >= 3) {
            throw new ConflictException("A member cannot have more than 3 active loans.");
        }

        Loan loan = new Loan(null, bookId, memberId, clock.instant(), null);

        Loan savedLoan = loans.save(loan);

        Book unavailableBook = new Book(book.id(), book.title(), book.author(), book.isbn(), false);

        books.save(unavailableBook);

        return savedLoan;

    }

    public Loan giveBack(long loanId) {

        Loan loan = loans.find(loanId).orElseThrow(NotFoundException::new);

        if (!loan.isActive()) {
            throw new ConflictException("This loan has already been returned.");
        }

        Book book = books.find(loan.bookId()).orElseThrow(NotFoundException::new);

        Loan returnedLoan = new Loan(loan.id(), loan.bookId(), loan.memberId(), loan.loanedAt(), clock.instant());

        loans.save(returnedLoan);

        Book availableBook = new Book(book.id(), book.title(), book.author(), book.isbn(), true);

        books.save(availableBook);

        return returnedLoan;

    }

}