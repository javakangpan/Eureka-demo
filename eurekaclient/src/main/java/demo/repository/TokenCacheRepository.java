package demo.repository;

import demo.model.TokenCache;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenCacheRepository extends CrudRepository<TokenCache,Long> {
    Optional<TokenCache> findOneBySessionId(String sessionId);
}
