package mu.mcbc.mcshares.repository;

import mu.mcbc.mcshares.domain.Individual;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Individual entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndividualRepository extends JpaRepository<Individual, String> {}
