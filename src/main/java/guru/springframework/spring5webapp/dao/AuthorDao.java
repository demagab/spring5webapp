package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;

public interface AuthorDao {

    Author getById(Long id);

    Author getByName(String firstName, String lastname);

    Author saveNewAuthor(Author author);

    Author updateAuthor(Author author);
}
