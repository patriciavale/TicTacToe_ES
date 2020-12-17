package es.g22.game_manager;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * GamesRepository
 */
@Repository
public interface GamesRepository extends JpaRepository<Game, Long>{

    @Query(value = "SELECT c FROM Game c WHERE c.finished = true")
    List<Game> getFinished();

    @Query(value = "SELECT c FROM Game c WHERE c.finished = false")
    List<Game> getNotFinished();

    
}