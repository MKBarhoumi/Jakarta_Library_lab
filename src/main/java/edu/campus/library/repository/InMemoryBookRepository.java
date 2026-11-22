package edu.campus.library.repository;
import edu.campus.library.domain.Book;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class InMemoryBookRepository implements BookRepository {

    private final Map<Long, Book> store =
            new ConcurrentHashMap<>();

    private final AtomicLong ids =
            new AtomicLong();

    @Override
    public List<Book> findAll() {

        return List.copyOf(store.values());

    }

    @Override
    public Optional<Book> find(long id) {

        return Optional.ofNullable(store.get(id));

    }

    @Override
    public Book save(Book book) {

        Long id = book.id();

        if (id == null) {
            id = ids.incrementAndGet();
        }

        Book saved = new Book(
                id,
                book.title(),
                book.author(),
                book.isbn(),
                book.available()
        );

        store.put(id, saved);

        return saved;

    }

    @Override
    public boolean delete(long id) {

        return store.remove(id) != null;

    }

}