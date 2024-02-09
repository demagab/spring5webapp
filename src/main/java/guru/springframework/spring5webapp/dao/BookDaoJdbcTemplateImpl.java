package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDaoJdbcTemplateImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;
    private final AuthorDao authorDao;

    public BookDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate, AuthorDao authorDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorDao = authorDao;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return null;
    }

    @Override
    public Book getById(Long id) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT * FROM book WHERE id = ? LIMIT 1", new BookMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Book getByTitle(String title) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM book WHERE title LIKE ? LIMIT 1", new BookMapper(), title);
    }

    @Override
    public Book findBookByTitleCriteria(String title) {
        return null;
    }

    @Override
    public Book findBookByTitleSqlNative(String title) {
        return null;
    }

    @Override
    public Book saveNew(Book book) {
        this.jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)", book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthor() != null ? book.getAuthor().getId() : null);

        Long savedId = this.jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class); //MyQSL thing
        return this.getById(savedId);
    }

    @Override
    public Book update(Book book) {
        this.jdbcTemplate.update("UPDATE book SET isbn = ?, publisher = ?, title = ?, author_id = ? WHERE id = ? LIMIT 1", book.getIsbn(), book.getPublisher(), book.getTitle(), (book.getAuthor() != null) ? book.getAuthor().getId(): null, book.getId());
        return this.getById(book.getId());
    }

    @Override
    public void deleteById(Long id) {
        this.jdbcTemplate.update("DELETE FROM book WHERE id = ? LIMIT 1", id);
    }
}
