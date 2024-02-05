package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {

    private final DataSource source;

    public AuthorDaoImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public List<Author> findAll() {
        return null;
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastName) {
        return null;
    }

    @Override
    public Author getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author WHERE id = ? LIMIT 1");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromRS(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.closeAll(resultSet, statement, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Author getByName(String firstName, String lastname) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("SELECT * FROM author WHERE first_name LIKE ? AND lastname LIKE ? LIMIT 1");
            statement.setString(1, firstName);
            statement.setString(2, lastname);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return getAuthorFromRS(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.closeAll(resultSet, statement, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Author saveNewAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("INSERT INTO author(first_name, lastname) VALUES (?, ?)");
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastname());
            statement.execute();

            Statement statement1 = connection.createStatement();
            resultSet = statement1.executeQuery("SELECT LAST_INSERT_ID()"); //MyQSL thing

            if (resultSet.next()) {
                Long savedId = resultSet.getLong(1);
                return this.getById(savedId);
            }

            statement1.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.closeAll(resultSet, statement, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Author updateAuthor(Author author) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("UPDATE author SET first_name = ?, lastname = ? WHERE id = ? LIMIT 1");
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastname());
            statement.setLong(3, author.getId());
            statement.execute();

            return this.getById(author.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.closeAll(resultSet, statement, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("DELETE FROM author WHERE id = ? LIMIT 1");
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                this.closeAll(null, statement, connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Author getAuthorFromRS(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setLastname(resultSet.getString("lastname"));

        return author;
    }

    private void closeAll(ResultSet resultSet, PreparedStatement statement, Connection connection) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

}
