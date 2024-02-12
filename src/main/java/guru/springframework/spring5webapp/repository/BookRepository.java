package guru.springframework.spring5webapp.repository;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findFirstBookByIsbn(String isbn);

    Book findFirstBookByTitleLike(String title);

    @Query("SELECT b FROM Book b WHERE b.title = ?1")
    List<Book> findBookByTitleWithQuery(String title);

    //Can also use named queries
}
