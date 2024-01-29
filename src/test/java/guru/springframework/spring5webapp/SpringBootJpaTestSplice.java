package guru.springframework.spring5webapp;

import guru.springframework.spring5webapp.domain.Book;
import guru.springframework.spring5webapp.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class SpringBootJpaTestSplice {
    @Autowired
    BookRepository bookRepository;

    @Test
    public void testJpaTestSplice() {
        long countBefore = bookRepository.count();
        bookRepository.save(new Book("Title test", "123456", "No one", null));
        long countAfter = bookRepository.count();

        assertThat(countBefore).isLessThan(countAfter);
    }
}
