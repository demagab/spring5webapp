package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import guru.springframework.spring5webapp.repository.AuthorRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Primary //This annotation chooses this bean by default since we now have two implementations of the same interface
public class AuthorDaoJpaImpl implements AuthorDao {
    private final AuthorRepository authorRepository;

    public AuthorDaoJpaImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
        return this.authorRepository.findAll();
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastName) {
        return this.authorRepository.findAuthorByLastnameLike(lastName);
    }

    @Override
    public Author getById(Long id) {
        return this.authorRepository.getOne(id);
    }

    @Override
    public Author getByName(String firstName, String lastname) {
        return this.authorRepository.findFirstAuthorByFirstNameAndLastname(firstName, lastname);
    }

    @Override
    public Author findAuthorByNameCriteria(String firstName, String lastName) {
        return null;
    }

    @Override
    public Author findAuthorByNameSqlNative(String firstName, String lastName) {
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        return this.authorRepository.save(author);
    }

    @Transactional //Work with transaction, so every call is not gonna be its own transaction
    @Override
    public Author updateAuthor(Author author) {
        Author found = this.authorRepository.getOne(author.getId());
        found.setLastname(author.getLastname());
        found.setFirstName(author.getFirstName());
        return this.authorRepository.save(found);
    }

    @Override
    public void deleteById(Long id) {
        this.authorRepository.deleteById(id); //org.hibernate.LazyInitializationException: could not initialize proxy
    }
}
