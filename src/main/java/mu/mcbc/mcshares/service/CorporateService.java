package mu.mcbc.mcshares.service;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Corporate;

/**
 * Service Interface for managing {@link Corporate}.
 */
public interface CorporateService {
    /**
     * Save a corporate.
     *
     * @param corporate the entity to save.
     * @return the persisted entity.
     */
    Corporate save(Corporate corporate);

    List<Corporate> saveAll(List<Corporate> corporate);

    /**
     * Partially updates a corporate.
     *
     * @param corporate the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Corporate> partialUpdate(Corporate corporate);

    /**
     * Get all the corporates.
     *
     * @return the list of entities.
     */
    List<Corporate> findAll();

    /**
     * Get the "id" corporate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Corporate> findOne(String id);

    /**
     * Delete the "id" corporate.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
