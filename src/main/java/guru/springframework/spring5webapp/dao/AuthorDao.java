package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;

import java.util.List;

public interface AuthorDao {
    List<Author> findAll();

    List<Author> listAuthorByLastNameLike(String lastName);

    Author getById(Long id);

    Author getByName(String firstName, String lastname);
    
    Author findAuthorByNameCriteria(String firstName, String lastName);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);

    void deleteById(Long id);
}
