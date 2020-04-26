package demo.repository;

import demo.test.logging.Log;
import demo.test.logging.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Logs,Long>, JpaSpecificationExecutor<Log> {

}
