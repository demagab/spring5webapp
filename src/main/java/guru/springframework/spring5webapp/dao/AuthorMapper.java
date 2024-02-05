package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import guru.springframework.spring5webapp.domain.Book;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuthorMapper implements RowMapper<Author> {
    @Override
    public Author mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("author.id"));
        author.setFirstName(resultSet.getString("author.first_name"));
        author.setLastname(resultSet.getString("author.lastname"));

        try {
            resultSet.findColumn("book.id");
            author.setBooks(new ArrayList<>());
            do {
                if (resultSet.getString("book.id") != null) {
                    author.getBooks().add(this.mapBookForAuthor(resultSet, author));
                }
            } while (resultSet.next());
        } catch (SQLException ignored) {}

        return author;
    }

    private Book mapBookForAuthor(ResultSet resultSet, Author author) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("book.id"));
        book.setIsbn(resultSet.getString("book.isbn"));
        book.setPublisher(resultSet.getString("book.publisher"));
        book.setTitle(resultSet.getString("book.title"));
        book.setAuthor(author);

        return book;
    }
}
