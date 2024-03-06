package com.gameplanner.game;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;

    @PostMapping("/platforms")
    public ResponseEntity insertPlatforms(){
        gameService.insertPlatforms();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/games")
    public ResponseEntity insertGames(){
        gameService.insertGames();
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/games/{year}/{month}")
    public ResponseEntity<GameResponse> getGameList(@RequestParam int page, @RequestParam int size, @PathVariable @Min(2000) int year, @PathVariable @Min(1) @Max(12) int month){
        GameResponse gameList = gameService.getGameList(page, size, year, month);
        return new ResponseEntity(gameList, HttpStatus.OK);
    }

    @PostMapping("/cover")
    public ResponseEntity insertCover(){
        gameService.insertCover();
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/screenshots")
    public ResponseEntity insertScreenshots(){
        gameService.insertScreenshots();
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("videos")
    public ResponseEntity insertVideos(){
        gameService.insertVideos();
        return new ResponseEntity(HttpStatus.OK);
    }

}
