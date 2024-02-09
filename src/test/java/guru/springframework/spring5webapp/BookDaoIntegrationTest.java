package guru.springframework.spring5webapp;

import guru.springframework.spring5webapp.dao.BookDao;
import guru.springframework.spring5webapp.domain.Author;
import guru.springframework.spring5webapp.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@ComponentScan(basePackages = {"guru.springframework.spring5webapp.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Test
    public void testFindAll() {
        List<Book> books = bookDao.findAll();

        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThan(0);
    }

    @Test
    public void findByIsbn() {
        Book book = bookDao.findByIsbn("123");

        assertThat(book).isNotNull();
        assertThat(book.getIsbn()).isEqualTo("123");
    }

    @Test
    public void testGet() {
        Book book = bookDao.getById(1L);

        assertThat(book).isNotNull();
    }

    @Test
    public void testGetByTitle() {
        Book book = bookDao.getByTitle("test");

        assertThat(book).isNotNull();
    }

    @Test
    public void testFindBookByTitleCriteria() {
        Book book = bookDao.findBookByTitleCriteria("test");

        assertThat(book).isNotNull();
    }

    @Test
    public void testFindBookByTitleSqlNative() {
        Book book = bookDao.findBookByTitleSqlNative("test");

        assertThat(book).isNotNull();
    }

    @Test
    public void testSave() {
        Book book = new Book("test", "000", "publisher", null);
        Book saved = bookDao.saveNew(book);

        assertThat(saved).isNotNull();
    }

    @Test
    public void testUpdate() {
        Book book = new Book("t-edit", "000", "publisher", new Author("Author", "Test"));
        Book saved = bookDao.saveNew(book);

        saved.setTitle("test-edited");
        Book updated = bookDao.update(saved);

        assertThat(updated).isNotNull();
    }

    @Test
    public void testDelete() {
        Book book = new Book("TO DELETE", "000", "publisher", null);
        Book saved = bookDao.saveNew(book);

        bookDao.deleteById(saved.getId());

        assertThat(bookDao.getById(saved.getId())).isNull();
    }
}
