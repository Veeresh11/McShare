package mu.mcbc.mcshares.repository;

import java.util.List;
import javax.persistence.EntityManager;
import mu.mcbc.mcshares.domain.Customer;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

public class CustomerRepositoryImpl implements CustomerRepositoryExt {

    private final EntityManager entityManager;

    public CustomerRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> search(String keywords) {
        final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        // Search query builder
        final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Customer.class).get();

        // Use a boolean junction and then add queries to it
        //		final BooleanJunction<BooleanJunction> outer = queryBuilder.bool();
        //		outer.must(queryBuilder.keyword().onFields("name").matching(keywords).createQuery());
        //
        //		@SuppressWarnings("unchecked")
        //		List<Customer> resultList = fullTextEntityManager.createFullTextQuery(outer.createQuery(), Customer.class)
        //				.getResultList();

        Query query = queryBuilder.keyword().onField("name").matching(keywords).createQuery();

        List<Customer> resultList = fullTextEntityManager.createFullTextQuery(query, Customer.class).getResultList();

        return resultList;
    }
}
