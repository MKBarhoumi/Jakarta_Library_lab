package edu.campus.library.service;

import edu.campus.library.domain.Book;
import edu.campus.library.domain.NewBook;
import edu.campus.library.repository.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
@Logged
public class BookService {

    private BookRepository repository;
    protected BookService() {
    }

    @Inject
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Book find(long id) {
        return repository.find(id).orElseThrow(NotFoundException::new);
    }

    public Book create(NewBook request) {
        Book book = new Book(null, request.title(), request.author(), request.isbn(), true);
        return repository.save(book);
    }

    public Book update(long id, NewBook request) {
        Book existing = find(id);
        Book updated = new Book(id, request.title(), request.author(), request.isbn(), existing.available());
        return repository.save(updated);
    }

    public void delete(long id) {
        find(id);
        repository.delete(id);
    }

}