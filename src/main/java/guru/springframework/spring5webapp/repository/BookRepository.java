package guru.springframework.spring5webapp.repository;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findFirstBookByIsbn(String isbn);

    Book findFirstBookByTitleLike(String title);
}
