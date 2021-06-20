package mu.mcbc.mcshares.repository;

import mu.mcbc.mcshares.domain.Shares;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Shares entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SharesRepository extends JpaRepository<Shares, Long> {}
