package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {

    private final DataSource source;
    private final AuthorDao authorDao;

    public BookDaoImpl(DataSource source, AuthorDao authorDao) {
        this.source = source;
        this.authorDao = authorDao;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public Book findByIsbn(String isbn) {
        return null;
    }

    @Override
    public Book getById(Long id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("SELECT * FROM book WHERE id = ? LIMIT 1");
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return this.getFromRS(resultSet);
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
    public Book getByTitle(String title) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("SELECT * FROM book WHERE title LIKE ? LIMIT 1");
            statement.setString(1, title);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return this.getFromRS(resultSet);
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
    public Book findBookByTitleCriteria(String title) {
        return null;
    }

    @Override
    public Book findBookByTitleSqlNative(String title) {
        return null;
    }

    @Override
    public Book saveNew(Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)");
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            if (book.getAuthor() != null) {
                statement.setLong(4, this.authorDao.saveNewAuthor(book.getAuthor()).getId());
            } else {
                statement.setNull(4, Types.NULL);
            }
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
    public Book update(Book book) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = source.getConnection();
            statement = connection.prepareStatement("UPDATE book SET isbn = ?, publisher = ?, title = ?, author_id = ? WHERE id = ? LIMIT 1");
            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getPublisher());
            statement.setString(3, book.getTitle());
            if (book.getAuthor() != null) {
                statement.setLong(4, book.getAuthor().getId());
            }
            statement.setLong(5, book.getId());
            statement.execute();

            return this.getById(book.getId());
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
            statement = connection.prepareStatement("DELETE FROM book WHERE id = ? LIMIT 1");
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

    private Book getFromRS(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(authorDao.getById(resultSet.getLong("author_id")));

        return book;
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
