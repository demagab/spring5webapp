package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;
import guru.springframework.spring5webapp.repository.BookRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Primary //This annotation chooses this bean by default since we now have two implementations of the same interface
public class BookDaoJpaImpl implements BookDao {
    private final BookRepository bookRepository;

    public BookDaoJpaImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return this.bookRepository.findAll();
    }

    @Override
    public Book findByIsbn(String isbn) {
        return this.bookRepository.findFirstBookByIsbn(isbn);
    }

    @Override
    public Book getById(Long id) {
        return this.bookRepository.getOne(id);
    }

    @Override
    public Book getByTitle(String title) {
        return this.bookRepository.findFirstBookByTitleLike(title);
    }

    @Override
    public Book findBookByTitleCriteria(String title) {
        return null;
    }

    @Override
    public Book findBookByTitleSqlNative(String title) {
        return this.bookRepository.findBookByTitleWithQuery(title).get(0);
    }

    @Override
    public Book saveNew(Book book) {
        return this.bookRepository.save(book);
    }

    @Transactional //Work with transaction, so every call is not gonna be its own transaction
    @Override
    public Book update(Book book) {
        Book found = this.bookRepository.getOne(book.getId());
        found.setAuthor(book.getAuthor());
        found.setPublisher(book.getPublisher());
        found.setIsbn(book.getIsbn());

        return this.bookRepository.save(found);
    }

    @Override
    public void deleteById(Long id) {
        this.bookRepository.deleteById(id);
    }
}
