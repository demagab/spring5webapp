package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;

public interface AuthorDao {

    Author getById(Long id);
}
