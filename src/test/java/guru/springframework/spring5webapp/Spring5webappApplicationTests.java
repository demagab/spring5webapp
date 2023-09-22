package guru.springframework.spring5webapp;

import guru.springframework.spring5webapp.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Spring5webappApplicationTests {

	@Autowired
	BookRepository bookRepository;

	@Test
	public void testBookRepository() {
		long count = bookRepository.count();

		assertThat(count).isGreaterThan(0);
	}

	@Test
	public void contextLoads() {
	}

}
