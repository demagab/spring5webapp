package guru.springframework.spring5webapp;

import guru.springframework.spring5webapp.dao.AuthorDao;
import guru.springframework.spring5webapp.domain.Author;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@ComponentScan(basePackages = {"guru.springframework.spring5webapp.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoIntegrationTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    public void testGetAuthor() {
        Author author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }

    @Test
    public void testGetAuthorByName() {
        Author author = authorDao.getByName("john", "doe");

        assertThat(author).isNotNull();
    }

    @Test
    public void testSaveAuthor() {
        Author author = new Author("john", "doe");
        Author saved = authorDao.saveNewAuthor(author);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    public void testUpdateAuthor() {
        Author author = new Author("john", "d");
        Author saved = authorDao.saveNewAuthor(author);

        saved.setLastname("doe upd");
        Author updated = authorDao.updateAuthor(saved);

        assertThat(updated).isNotNull();
        assertThat(updated.getLastname()).isEqualTo("doe upd");
    }

    @Test
    public void testDeleteAuthor() {
        Author author = new Author("author", "TO DELETE");
        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteById(saved.getId());

        assertThat(authorDao.getById(saved.getId())).isNull();
    }
}
