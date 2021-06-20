package mu.mcbc.mcshares.service.impl;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Individual;
import mu.mcbc.mcshares.repository.IndividualRepository;
import mu.mcbc.mcshares.service.IndividualService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Individual}.
 */
@Service
@Transactional
public class IndividualServiceImpl implements IndividualService {

    private final Logger log = LoggerFactory.getLogger(IndividualServiceImpl.class);

    private final IndividualRepository individualRepository;

    public IndividualServiceImpl(IndividualRepository individualRepository) {
        this.individualRepository = individualRepository;
    }

    @Override
    public Individual save(Individual individual) {
        log.debug("Request to save Individual : {}", individual);
        return individualRepository.save(individual);
    }

    @Override
    public Optional<Individual> partialUpdate(Individual individual) {
        log.debug("Request to partially update Individual : {}", individual);

        return individualRepository
            .findById(individual.getId())
            .map(
                existingIndividual -> {
                    if (individual.getDob() != null) {
                        existingIndividual.setDob(individual.getDob());
                    }

                    return existingIndividual;
                }
            )
            .map(individualRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Individual> findAll() {
        log.debug("Request to get all Individuals");
        return individualRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Individual> findOne(String id) {
        log.debug("Request to get Individual : {}", id);
        return individualRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Individual : {}", id);
        individualRepository.deleteById(id);
    }

    @Override
    public List<Individual> saveAll(List<Individual> individual) {
        log.debug("Request to save all Individuals");
        return individualRepository.saveAll(individual);
    }
}
