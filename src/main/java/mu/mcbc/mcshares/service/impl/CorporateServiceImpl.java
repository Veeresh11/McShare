package mu.mcbc.mcshares.service.impl;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Corporate;
import mu.mcbc.mcshares.repository.CorporateRepository;
import mu.mcbc.mcshares.service.CorporateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Corporate}.
 */
@Service
@Transactional
public class CorporateServiceImpl implements CorporateService {

    private final Logger log = LoggerFactory.getLogger(CorporateServiceImpl.class);

    private final CorporateRepository corporateRepository;

    public CorporateServiceImpl(CorporateRepository corporateRepository) {
        this.corporateRepository = corporateRepository;
    }

    @Override
    public Corporate save(Corporate corporate) {
        log.debug("Request to save Corporate : {}", corporate);
        return corporateRepository.save(corporate);
    }

    @Override
    public Optional<Corporate> partialUpdate(Corporate corporate) {
        log.debug("Request to partially update Corporate : {}", corporate);

        return corporateRepository
            .findById(corporate.getId())
            .map(
                existingCorporate -> {
                    if (corporate.getDateIncorp() != null) {
                        existingCorporate.setDateIncorp(corporate.getDateIncorp());
                    }
                    if (corporate.getRegNo() != null) {
                        existingCorporate.setRegNo(corporate.getRegNo());
                    }

                    return existingCorporate;
                }
            )
            .map(corporateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Corporate> findAll() {
        log.debug("Request to get all Corporates");
        return corporateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Corporate> findOne(String id) {
        log.debug("Request to get Corporate : {}", id);
        return corporateRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Corporate : {}", id);
        corporateRepository.deleteById(id);
    }

    @Override
    public List<Corporate> saveAll(List<Corporate> corporate) {
        log.debug("Request to save all Corporates");
        return corporateRepository.saveAll(corporate);
    }
}
