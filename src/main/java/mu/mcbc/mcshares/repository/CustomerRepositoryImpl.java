package mu.mcbc.mcshares.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import mu.mcbc.mcshares.domain.Customer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class CustomerRepositoryImpl implements CustomerRepositoryExt {

    @Autowired
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Customer> search(final String keywords) {
        final FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        // Search query builder
        final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Customer.class).get();

        // Use a boolean junction and then add queries to it
        final BooleanJunction<BooleanJunction> outer = queryBuilder.bool();
        outer.must(queryBuilder.keyword().onFields("name").matching(keywords).createQuery());

        @SuppressWarnings("unchecked")
        List<Customer> resultList = fullTextEntityManager.createFullTextQuery(outer.createQuery(), Customer.class).getResultList();
        return resultList;
    }
}
