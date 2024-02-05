package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorExtractor implements ResultSetExtractor<Author> {
    public Author extractData(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new AuthorMapper().mapRow(resultSet, 0);
        } else {
            return null;
        }
    }
}
