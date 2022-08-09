package startup.ci.app.restapi.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import startup.ci.app.restapi.Model.TransactionProcessor;

public interface TransactionProcessorRepo extends JpaRepository<TransactionProcessor, Long> {

}
