package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Primary //This annotation chooses this bean by default since we now have two implementations of the same interface
public class AuthorDaoJdbcTemplateImpl implements AuthorDao {
    private final JdbcTemplate jdbcTemplate;

    public AuthorDaoJdbcTemplateImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getById(Long id) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT * FROM author WHERE id = ?", this.getRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Author getByName(String firstName, String lastname) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM author WHERE first_name LIKE ? AND lastname LIKE ? LIMIT 1", getRowMapper(), firstName, lastname);
    }

    @Override
    public Author saveNewAuthor(Author author) {
        this.jdbcTemplate.update("INSERT INTO author(first_name, lastname) VALUES (?, ?)", author.getFirstName(), author.getLastname());

        Long savedId = this.jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class); //MyQSL thing
        return this.getById(savedId);
    }

    @Override
    public Author updateAuthor(Author author) {
        this.jdbcTemplate.update("UPDATE author SET first_name = ?, lastname = ? WHERE id = ? LIMIT 1", author.getFirstName(), author.getLastname(), author.getId());
        return this.getById(author.getId());
    }

    @Override
    public void deleteById(Long id) {
        this.jdbcTemplate.update("DELETE FROM author WHERE id = ? LIMIT 1", id);
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }
}
