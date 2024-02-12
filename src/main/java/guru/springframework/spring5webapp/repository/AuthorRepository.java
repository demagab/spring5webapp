package guru.springframework.spring5webapp.repository;

import guru.springframework.spring5webapp.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAll();

    Author findFirstAuthorByFirstNameAndLastname(String firstName, String lastname);

    List<Author> findAuthorByLastnameLike(String lastname);
}
