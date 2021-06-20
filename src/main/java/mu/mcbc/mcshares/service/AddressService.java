package mu.mcbc.mcshares.service;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Address;

/**
 * Service Interface for managing {@link Address}.
 */
public interface AddressService {
    /**
     * Save a address.
     *
     * @param address the entity to save.
     * @return the persisted entity.
     */
    Address save(Address address);

    /**
     * Partially updates a address.
     *
     * @param address the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Address> partialUpdate(Address address);

    /**
     * Get all the addresses.
     *
     * @return the list of entities.
     */
    List<Address> findAll();

    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Address> findOne(Long id);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
