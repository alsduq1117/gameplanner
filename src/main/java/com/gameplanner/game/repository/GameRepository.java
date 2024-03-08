package com.gameplanner.game.repository;

import com.gameplanner.game.domain.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE g.firstReleaseDate > :startUnixTime AND g.firstReleaseDate < :nextMonthUnixTime")
    Page<Game> findAllByReleaseDateBetween(@Param("startUnixTime") long startUnixTime, @Param("nextMonthUnixTime") long nextMonthUnixTime, Pageable pageable);
}
