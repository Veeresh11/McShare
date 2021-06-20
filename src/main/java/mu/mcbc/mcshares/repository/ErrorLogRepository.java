package mu.mcbc.mcshares.repository;

import mu.mcbc.mcshares.domain.ErrorLog;
import mu.mcbc.mcshares.domain.Shares;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unused")
@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {}
