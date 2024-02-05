package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper implements RowMapper<Book> {
    @Autowired
    AuthorDao authorDao;

    @Override
    public Book mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setIsbn(resultSet.getString("isbn"));
        book.setPublisher(resultSet.getString("publisher"));
        book.setTitle(resultSet.getString("title"));
        try {
            book.setAuthor(this.authorDao.getById(resultSet.getLong("author_id")));
        } catch (Exception ignored) {}

        return book;
    }
}
