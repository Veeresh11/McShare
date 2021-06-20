package mu.mcbc.mcshares.service.impl;

import java.util.Optional;
import mu.mcbc.mcshares.domain.Customer;
import mu.mcbc.mcshares.domain.ErrorLog;
import mu.mcbc.mcshares.repository.CustomerRepository;
import mu.mcbc.mcshares.repository.ErrorLogRepository;
import mu.mcbc.mcshares.service.ErrorLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ErrorLogServiceImpl implements ErrorLogService {

    private final Logger log = LoggerFactory.getLogger(ErrorLogServiceImpl.class);

    private final ErrorLogRepository errorLogRepository;

    public ErrorLogServiceImpl(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    @Override
    public ErrorLog save(ErrorLog errLog) {
        log.debug("Request to save Error Log : {}", errLog);
        return errorLogRepository.save(errLog);
    }
}
