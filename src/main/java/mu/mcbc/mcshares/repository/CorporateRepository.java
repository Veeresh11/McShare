package mu.mcbc.mcshares.repository;

import mu.mcbc.mcshares.domain.Corporate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Corporate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorporateRepository extends JpaRepository<Corporate, String> {}
