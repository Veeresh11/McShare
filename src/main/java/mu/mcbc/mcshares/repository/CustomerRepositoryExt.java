package mu.mcbc.mcshares.repository;

import java.util.List;
import mu.mcbc.mcshares.domain.Customer;

public interface CustomerRepositoryExt {
    List<Customer> search(final String keywords);
}
