package co.com.bancolombia.galatea.repository;

import java.util.Optional;

import co.com.bancolombia.galatea.entity.ManuscriptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManuscriptRepository extends JpaRepository<ManuscriptEntity, Long> {
    
    Optional<ManuscriptEntity> findByHash(String hash);
}

