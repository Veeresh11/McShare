package mu.mcbc.mcshares.service;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Individual;

/**
 * Service Interface for managing {@link Individual}.
 */
public interface IndividualService {
    /**
     * Save a individual.
     *
     * @param individual the entity to save.
     * @return the persisted entity.
     */
    Individual save(Individual individual);

    List<Individual> saveAll(List<Individual> individual);

    /**
     * Partially updates a individual.
     *
     * @param individual the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Individual> partialUpdate(Individual individual);

    /**
     * Get all the individuals.
     *
     * @return the list of entities.
     */
    List<Individual> findAll();

    /**
     * Get the "id" individual.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Individual> findOne(String id);

    /**
     * Delete the "id" individual.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
