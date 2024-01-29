package guru.springframework.spring5webapp.bootstrap;

import guru.springframework.spring5webapp.domain.Book;
import guru.springframework.spring5webapp.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"local", "default"})
@Component
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Book bookDDD = new Book("Domain Driven Design", "123", "RandomPeople", null);
        System.out.println("Saving with ID: " + bookRepository.save(bookDDD).getId());

        Book bookSIA = new Book("SIA", "456", "Nobody", null);
        System.out.println("Saving with ID: " + bookRepository.save(bookSIA).getId());

        bookRepository.findAll().forEach(book -> {
            System.out.println("ID " + book.getId()+" Title: "+book.getTitle());
        });
    }
}
