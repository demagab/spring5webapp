package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import guru.springframework.spring5webapp.domain.Book;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
@Primary //This annotation chooses this bean by default since we now have two implementations of the same interface
public class BookDaoHibernateImpl implements BookDao {
    private final EntityManagerFactory entityManagerFactory;

    public BookDaoHibernateImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Book findByIsbn(String isbn) {
        EntityManager entityManager = this.getEntityManager();
        try {
            TypedQuery<Book> query = entityManager.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
            query.setMaxResults(1);
            query.setParameter("isbn", isbn);

            return query.getSingleResult();
        } catch (EmptyResultDataAccessException e) {
            return null;
        } finally {
            entityManager.close(); //Close transaction
        }
    }

    @Override
    public Book getById(Long id) {
        EntityManager em = this.getEntityManager();
        try {
            return em.find(Book.class, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } finally {
            em.close(); //Close transaction
        }
    }

    @Override
    public Book getByTitle(String title) {
        EntityManager em = this.getEntityManager();

        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class);
        query.setMaxResults(1);
        query.setParameter("title", title);
        Book book = query.getSingleResult();

        em.close(); //Close transaction
        return book;
    }

    @Override
    public Book saveNew(Book book) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin(); //Open transaction

        entityManager.persist(book); //Do operation

        entityManager.flush(); //Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
        return book;
    }

    @Override
    public Book update(Book book) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin(); //Open transaction

        entityManager.merge(book);
        Book updated = entityManager.find(Book.class, book.getId());

        entityManager.flush(); //Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
        return updated;
    }

    @Override
    public void deleteById(Long id) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin(); //Open transaction

        entityManager.remove(entityManager.find(Book.class, id));

        entityManager.flush(); //Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private EntityManager getEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }
}
