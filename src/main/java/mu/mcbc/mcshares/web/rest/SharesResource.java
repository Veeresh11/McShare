package mu.mcbc.mcshares.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Shares;
import mu.mcbc.mcshares.repository.SharesRepository;
import mu.mcbc.mcshares.service.SharesService;
import mu.mcbc.mcshares.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link mu.mcbc.mcshares.domain.Shares}.
 */
@RestController
@RequestMapping("/api")
public class SharesResource {

    private final Logger log = LoggerFactory.getLogger(SharesResource.class);

    private static final String ENTITY_NAME = "shares";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SharesService sharesService;

    private final SharesRepository sharesRepository;

    public SharesResource(SharesService sharesService, SharesRepository sharesRepository) {
        this.sharesService = sharesService;
        this.sharesRepository = sharesRepository;
    }

    /**
     * {@code POST  /shares} : Create a new shares.
     *
     * @param shares the shares to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shares, or with status {@code 400 (Bad Request)} if the shares has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shares")
    public ResponseEntity<Shares> createShares(@RequestBody Shares shares) throws URISyntaxException {
        log.debug("REST request to save Shares : {}", shares);
        if (shares.getId() != null) {
            throw new BadRequestAlertException("A new shares cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shares result = sharesService.save(shares);
        return ResponseEntity
            .created(new URI("/api/shares/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shares/:id} : Updates an existing shares.
     *
     * @param id the id of the shares to save.
     * @param shares the shares to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shares,
     * or with status {@code 400 (Bad Request)} if the shares is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shares couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shares/{id}")
    public ResponseEntity<Shares> updateShares(@PathVariable(value = "id", required = false) final Long id, @RequestBody Shares shares)
        throws URISyntaxException {
        log.debug("REST request to update Shares : {}, {}", id, shares);
        if (shares.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shares.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sharesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Shares result = sharesService.save(shares);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shares.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shares/:id} : Partial updates given fields of an existing shares, field will ignore if it is null
     *
     * @param id the id of the shares to save.
     * @param shares the shares to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shares,
     * or with status {@code 400 (Bad Request)} if the shares is not valid,
     * or with status {@code 404 (Not Found)} if the shares is not found,
     * or with status {@code 500 (Internal Server Error)} if the shares couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shares/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Shares> partialUpdateShares(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Shares shares
    ) throws URISyntaxException {
        log.debug("REST request to partial update Shares partially : {}, {}", id, shares);
        if (shares.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shares.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sharesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Shares> result = sharesService.partialUpdate(shares);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shares.getId().toString())
        );
    }

    /**
     * {@code GET  /shares} : get all the shares.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shares in body.
     */
    @GetMapping("/shares")
    public List<Shares> getAllShares() {
        log.debug("REST request to get all Shares");
        return sharesService.findAll();
    }

    /**
     * {@code GET  /shares/:id} : get the "id" shares.
     *
     * @param id the id of the shares to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shares, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shares/{id}")
    public ResponseEntity<Shares> getShares(@PathVariable Long id) {
        log.debug("REST request to get Shares : {}", id);
        Optional<Shares> shares = sharesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shares);
    }

    /**
     * {@code DELETE  /shares/:id} : delete the "id" shares.
     *
     * @param id the id of the shares to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shares/{id}")
    public ResponseEntity<Void> deleteShares(@PathVariable Long id) {
        log.debug("REST request to delete Shares : {}", id);
        sharesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
