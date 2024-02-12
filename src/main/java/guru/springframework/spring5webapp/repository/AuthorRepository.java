package guru.springframework.spring5webapp.repository;

import guru.springframework.spring5webapp.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findAll();

    Author findFirstAuthorByFirstNameAndLastname(String firstName, String lastname);

    List<Author> findAuthorByLastnameLike(String lastname);

    @Query("SELECT a FROM Author a WHERE a.firstName = :fN AND a.lastname = :lN") //Can also add parameter nativeQuery = true and use natives
    List<Author> findAuthorByFirstNameAndLastnameWithQuery(@Param("fN")String firstName, @Param("lN")String lastName);
}
