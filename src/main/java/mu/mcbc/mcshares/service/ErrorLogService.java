package mu.mcbc.mcshares.service;

import java.util.Optional;
import mu.mcbc.mcshares.domain.Customer;
import mu.mcbc.mcshares.domain.ErrorLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ErrorLogService {
    /**
     * Save a customer.
     *
     * @param customer the entity to save.
     * @return the persisted entity.
     */
    ErrorLog save(ErrorLog errlog);
}
