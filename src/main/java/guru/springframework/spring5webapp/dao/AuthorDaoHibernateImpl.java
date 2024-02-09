package guru.springframework.spring5webapp.dao;

import guru.springframework.spring5webapp.domain.Author;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Component
@Primary //This annotation chooses this bean by default since we now have two implementations of the same interface
public class AuthorDaoHibernateImpl implements AuthorDao {
    private final EntityManagerFactory entityManagerFactory;

    public AuthorDaoHibernateImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Author> findAll() {
        EntityManager entityManager = this.getEntityManager();

        try{
            TypedQuery<Author> query = entityManager.createNamedQuery("author_find_all", Author.class);

            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Author> listAuthorByLastNameLike(String lastName) {
        EntityManager entityManager = this.getEntityManager();

        try{
            //SAME as: TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a WHERE a.lastname like :lastname", Author.class);
            TypedQuery<Author> query = entityManager.createNamedQuery("author_find_by_last_name", Author.class);
            query.setParameter("lastname", "%"+lastName+"%");

            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Author getById(Long id) {
        EntityManager entityManager = this.getEntityManager();

        Author author = entityManager.find(Author.class, id);

        entityManager.close(); //Close transaction
        return author;
    }

    @Override
    public Author getByName(String firstName, String lastname) {
        EntityManager entityManager = this.getEntityManager();
        TypedQuery<Author> query = entityManager.createQuery("SELECT a FROM Author a WHERE a.firstName = :firstName AND a.lastname = :lastName", Author.class);

        query.setMaxResults(1);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastname);
        Author author = query.getSingleResult();

        entityManager.close(); //Close transaction
        return author;
    }

    @Override
    public Author findAuthorByNameCriteria(String firstName, String lastName) {
        // implementation with Criteria Query
        EntityManager entityManager = this.getEntityManager();

        try{
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);

            Root<Author> root = criteriaQuery.from(Author.class);

            ParameterExpression<String> firstNameParameter = criteriaBuilder.parameter(String.class);
            ParameterExpression<String> lastNameParameter = criteriaBuilder.parameter(String.class);

            Predicate firstNamePredicate = criteriaBuilder.equal(root.get("firstName"), firstNameParameter);
            Predicate lastNamePredicate = criteriaBuilder.equal(root.get("lastname"), lastNameParameter);

            criteriaQuery.select(root).where(criteriaBuilder.and(firstNamePredicate, lastNamePredicate));

            TypedQuery<Author> typedQuery = entityManager.createQuery(criteriaQuery).setMaxResults(1); //LIMIT 1
            typedQuery.setParameter(firstNameParameter, firstName);
            typedQuery.setParameter(lastNameParameter, lastName);

            return typedQuery.getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Author saveNewAuthor(Author author) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin(); //Open transaction

        entityManager.persist(author); //Do operation

        entityManager.flush(); //Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin(); //Open transaction

        entityManager.merge(author);
        Author updated = entityManager.find(Author.class, author.getId());

        entityManager.flush(); //Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
        return updated;
    }

    @Override
    public void deleteById(Long id) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin(); //Open transaction

        entityManager.remove(entityManager.find(Author.class, id));

        entityManager.flush(); //Commit and close transaction
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    private EntityManager getEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

}
