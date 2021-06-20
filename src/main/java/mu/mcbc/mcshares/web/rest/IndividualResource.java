package mu.mcbc.mcshares.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Individual;
import mu.mcbc.mcshares.repository.IndividualRepository;
import mu.mcbc.mcshares.service.IndividualService;
import mu.mcbc.mcshares.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mu.mcbc.mcshares.domain.Individual}.
 */
@RestController
@RequestMapping("/api")
public class IndividualResource {

    private final Logger log = LoggerFactory.getLogger(IndividualResource.class);

    private static final String ENTITY_NAME = "individual";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IndividualService individualService;

    private final IndividualRepository individualRepository;

    public IndividualResource(IndividualService individualService, IndividualRepository individualRepository) {
        this.individualService = individualService;
        this.individualRepository = individualRepository;
    }

    /**
     * {@code POST  /individuals} : Create a new individual.
     *
     * @param individual the individual to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new individual, or with status {@code 400 (Bad Request)} if the individual has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/individuals")
    public ResponseEntity<Individual> createIndividual(@RequestBody Individual individual) throws URISyntaxException {
        log.debug("REST request to save Individual : {}", individual);
        if (individual.getId() != null) {
            throw new BadRequestAlertException("A new individual cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Individual result = individualService.save(individual);
        return ResponseEntity
            .created(new URI("/api/individuals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /individuals/:id} : Updates an existing individual.
     *
     * @param id the id of the individual to save.
     * @param individual the individual to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individual,
     * or with status {@code 400 (Bad Request)} if the individual is not valid,
     * or with status {@code 500 (Internal Server Error)} if the individual couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/individuals/{id}")
    public ResponseEntity<Individual> updateIndividual(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Individual individual
    ) throws URISyntaxException {
        log.debug("REST request to update Individual : {}, {}", id, individual);
        if (individual.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, individual.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!individualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Individual result = individualService.save(individual);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, individual.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /individuals/:id} : Partial updates given fields of an existing individual, field will ignore if it is null
     *
     * @param id the id of the individual to save.
     * @param individual the individual to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated individual,
     * or with status {@code 400 (Bad Request)} if the individual is not valid,
     * or with status {@code 404 (Not Found)} if the individual is not found,
     * or with status {@code 500 (Internal Server Error)} if the individual couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/individuals/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Individual> partialUpdateIndividual(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Individual individual
    ) throws URISyntaxException {
        log.debug("REST request to partial update Individual partially : {}, {}", id, individual);
        if (individual.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, individual.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!individualRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Individual> result = individualService.partialUpdate(individual);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, individual.getId())
        );
    }

    /**
     * {@code GET  /individuals} : get all the individuals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of individuals in body.
     */
    @GetMapping("/individuals")
    public List<Individual> getAllIndividuals() {
        log.debug("REST request to get all Individuals");
        return individualService.findAll();
    }

    /**
     * {@code GET  /individuals/:id} : get the "id" individual.
     *
     * @param id the id of the individual to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the individual, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/individuals/{id}")
    public ResponseEntity<Individual> getIndividual(@PathVariable String id) {
        log.debug("REST request to get Individual : {}", id);
        Optional<Individual> individual = individualService.findOne(id);
        return ResponseUtil.wrapOrNotFound(individual);
    }

    /**
     * {@code DELETE  /individuals/:id} : delete the "id" individual.
     *
     * @param id the id of the individual to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/individuals/{id}")
    public ResponseEntity<Void> deleteIndividual(@PathVariable String id) {
        log.debug("REST request to delete Individual : {}", id);
        individualService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
