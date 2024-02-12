package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface BookDao {
    List<Book> findAll(Pageable pageable);

    List<Book> findAll();

    Book findByIsbn (String isbn);

    Book getById(Long id);

    Book getByTitle(String title);

    Book findBookByTitleCriteria(String title);

    Book findBookByTitleSqlNative(String title);

    Book saveNew(Book book);

    Book update(Book book);

    void deleteById(Long id);
}
