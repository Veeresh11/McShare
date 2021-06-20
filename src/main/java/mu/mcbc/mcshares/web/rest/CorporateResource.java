package mu.mcbc.mcshares.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Corporate;
import mu.mcbc.mcshares.repository.CorporateRepository;
import mu.mcbc.mcshares.service.CorporateService;
import mu.mcbc.mcshares.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mu.mcbc.mcshares.domain.Corporate}.
 */
@RestController
@RequestMapping("/api")
public class CorporateResource {

    private final Logger log = LoggerFactory.getLogger(CorporateResource.class);

    private static final String ENTITY_NAME = "corporate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CorporateService corporateService;

    private final CorporateRepository corporateRepository;

    public CorporateResource(CorporateService corporateService, CorporateRepository corporateRepository) {
        this.corporateService = corporateService;
        this.corporateRepository = corporateRepository;
    }

    /**
     * {@code POST  /corporates} : Create a new corporate.
     *
     * @param corporate the corporate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new corporate, or with status {@code 400 (Bad Request)} if the corporate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/corporates")
    public ResponseEntity<Corporate> createCorporate(@RequestBody Corporate corporate) throws URISyntaxException {
        log.debug("REST request to save Corporate : {}", corporate);
        //        if (corporate.getId() != null) {
        //            throw new BadRequestAlertException("A new corporate cannot already have an ID", ENTITY_NAME, "idexists");
        //        }
        Corporate result = corporateService.save(corporate);
        return ResponseEntity
            .created(new URI("/api/corporates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /corporates/:id} : Updates an existing corporate.
     *
     * @param id the id of the corporate to save.
     * @param corporate the corporate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corporate,
     * or with status {@code 400 (Bad Request)} if the corporate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the corporate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/corporates/{id}")
    public ResponseEntity<Corporate> updateCorporate(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Corporate corporate
    ) throws URISyntaxException {
        log.debug("REST request to update Corporate : {}, {}", id, corporate);
        if (corporate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corporate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corporateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<Corporate> existingCorp = corporateRepository.findById(id);
        System.out.println("SHAres: " + existingCorp.get().getShare().getNumShares() + " --- " + corporate.getShare().getNumShares());
        if (!existingCorp.get().getShare().getNumShares().equals(corporate.getShare().getNumShares())) {
            throw new BadRequestAlertException("Number of Shares is not amendable", ENTITY_NAME, "numofshareserror");
        }

        Corporate result = corporateService.save(corporate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, corporate.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /corporates/:id} : Partial updates given fields of an existing corporate, field will ignore if it is null
     *
     * @param id the id of the corporate to save.
     * @param corporate the corporate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated corporate,
     * or with status {@code 400 (Bad Request)} if the corporate is not valid,
     * or with status {@code 404 (Not Found)} if the corporate is not found,
     * or with status {@code 500 (Internal Server Error)} if the corporate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/corporates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Corporate> partialUpdateCorporate(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Corporate corporate
    ) throws URISyntaxException {
        log.debug("REST request to partial update Corporate partially : {}, {}", id, corporate);
        if (corporate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, corporate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!corporateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Corporate> result = corporateService.partialUpdate(corporate);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, corporate.getId())
        );
    }

    /**
     * {@code GET  /corporates} : get all the corporates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of corporates in body.
     */
    @GetMapping("/corporates")
    public List<Corporate> getAllCorporates() {
        log.debug("REST request to get all Corporates");
        return corporateService.findAll();
    }

    /**
     * {@code GET  /corporates/:id} : get the "id" corporate.
     *
     * @param id the id of the corporate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the corporate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/corporates/{id}")
    public ResponseEntity<Corporate> getCorporate(@PathVariable String id) {
        log.debug("REST request to get Corporate : {}", id);
        Optional<Corporate> corporate = corporateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(corporate);
    }

    /**
     * {@code DELETE  /corporates/:id} : delete the "id" corporate.
     *
     * @param id the id of the corporate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/corporates/{id}")
    public ResponseEntity<Void> deleteCorporate(@PathVariable String id) {
        log.debug("REST request to delete Corporate : {}", id);
        corporateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id)).build();
    }
}
