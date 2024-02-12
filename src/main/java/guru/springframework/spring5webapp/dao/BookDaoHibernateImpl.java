package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Book;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Component
public class BookDaoHibernateImpl implements BookDao {
    private final EntityManagerFactory entityManagerFactory;

    public BookDaoHibernateImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Book> findAll() {
        EntityManager entityManager = this.getEntityManager();

        try{
            TypedQuery<Book> query = entityManager.createNamedQuery("book_find_all", Book.class);

            return query.getResultList();
        } finally {
            entityManager.close();
        }
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

        TypedQuery<Book> query = em.createNamedQuery("book_find_by_title", Book.class);
        query.setMaxResults(1);
        query.setParameter("title", title);
        Book book = query.getSingleResult();

        em.close(); //Close transaction
        return book;
    }

    @Override
    public Book findBookByTitleCriteria(String title) {
        // implementation with Criteria Query
        EntityManager entityManager = this.getEntityManager();

        try{
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);

            Root<Book> root = criteriaQuery.from(Book.class);

            ParameterExpression<String> titleParameter = criteriaBuilder.parameter(String.class);

            Predicate titlePredicate = criteriaBuilder.equal(root.get("title"), titleParameter);

            criteriaQuery.select(root).where(criteriaBuilder.and(titlePredicate));

            TypedQuery<Book> typedQuery = entityManager.createQuery(criteriaQuery).setMaxResults(1); //LIMIT 1
            typedQuery.setParameter(titleParameter, title);

            return typedQuery.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Book findBookByTitleSqlNative(String title) {
        // implementation with SQL native
        EntityManager entityManager = this.getEntityManager();

        try{
            Query query = entityManager.createNativeQuery("SELECT * FROM book WHERE title = :title LIMIT 1", Book.class);
            query.setParameter("title", title); //Can used named biding :fn or positional biding ? but can't mix them in a query

            return (Book) query.getSingleResult();
        } finally {
            entityManager.close();
        }
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
