package guru.springframework.spring5webapp.repository;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
