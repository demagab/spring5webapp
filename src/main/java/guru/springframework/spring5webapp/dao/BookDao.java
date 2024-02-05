package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();

    Book findByIsbn (String isbn);

    Book getById(Long id);

    Book getByTitle(String title);

    Book saveNew(Book book);

    Book update(Book book);

    void deleteById(Long id);
}
