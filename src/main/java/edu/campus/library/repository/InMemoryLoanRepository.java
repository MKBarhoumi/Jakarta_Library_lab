package edu.campus.library.repository;

import edu.campus.library.domain.Loan;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class InMemoryLoanRepository implements LoanRepository {

    private final Map<Long, Loan> store = new ConcurrentHashMap<>();

    private final AtomicLong ids = new AtomicLong();

    @Override
    public List<Loan> findAll() {

        return List.copyOf(store.values());

    }

    @Override
    public Optional<Loan> find(long id) {

        return Optional.ofNullable(store.get(id));

    }

    @Override
    public Loan save(Loan loan) {

        Long id = loan.id();

        if (id == null) {
            id = ids.incrementAndGet();
        }

        Loan saved = new Loan(
                id,
                loan.bookId(),
                loan.memberId(),
                loan.loanedAt(),
                loan.returnedAt()
        );

        store.put(id, saved);

        return saved;

    }

    @Override
    public List<Loan> findActiveByBookId(long bookId) {

        return store.values().stream().filter(loan -> loan.bookId() == bookId).filter(Loan::isActive).toList();

    }

    @Override
    public long countActiveByMemberId(long memberId) {

        return store.values().stream().filter(loan -> loan.memberId() == memberId).filter(Loan::isActive).count();

    }

}