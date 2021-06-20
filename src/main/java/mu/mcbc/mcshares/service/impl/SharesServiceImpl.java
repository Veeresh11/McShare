package mu.mcbc.mcshares.service.impl;

import java.util.List;
import java.util.Optional;
import mu.mcbc.mcshares.domain.Shares;
import mu.mcbc.mcshares.repository.SharesRepository;
import mu.mcbc.mcshares.service.SharesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Shares}.
 */
@Service
@Transactional
public class SharesServiceImpl implements SharesService {

    private final Logger log = LoggerFactory.getLogger(SharesServiceImpl.class);

    private final SharesRepository sharesRepository;

    public SharesServiceImpl(SharesRepository sharesRepository) {
        this.sharesRepository = sharesRepository;
    }

    @Override
    public Shares save(Shares shares) {
        log.debug("Request to save Shares : {}", shares);
        return sharesRepository.save(shares);
    }

    @Override
    public Optional<Shares> partialUpdate(Shares shares) {
        log.debug("Request to partially update Shares : {}", shares);

        return sharesRepository
            .findById(shares.getId())
            .map(
                existingShares -> {
                    if (shares.getNumShares() != null) {
                        existingShares.setNumShares(shares.getNumShares());
                    }
                    if (shares.getSharePrice() != null) {
                        existingShares.setSharePrice(shares.getSharePrice());
                    }

                    return existingShares;
                }
            )
            .map(sharesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shares> findAll() {
        log.debug("Request to get all Shares");
        return sharesRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shares> findOne(Long id) {
        log.debug("Request to get Shares : {}", id);
        return sharesRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Shares : {}", id);
        sharesRepository.deleteById(id);
    }
}
