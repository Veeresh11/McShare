package mu.mcbc.mcshares.service;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Shares;

/**
 * Service Interface for managing {@link Shares}.
 */
public interface SharesService {
    /**
     * Save a shares.
     *
     * @param shares the entity to save.
     * @return the persisted entity.
     */
    Shares save(Shares shares);

    /**
     * Partially updates a shares.
     *
     * @param shares the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Shares> partialUpdate(Shares shares);

    /**
     * Get all the shares.
     *
     * @return the list of entities.
     */
    List<Shares> findAll();

    /**
     * Get the "id" shares.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Shares> findOne(Long id);

    /**
     * Delete the "id" shares.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
