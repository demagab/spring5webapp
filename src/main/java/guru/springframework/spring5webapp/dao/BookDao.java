package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;

public interface BookDao {
    Book getById(Long id);

    Book getByTitle(String title);

    Book saveNew(Book book);

    Book update(Book book);

    void deleteById(Long id);
}
