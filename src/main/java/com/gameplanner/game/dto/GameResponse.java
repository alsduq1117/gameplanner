package com.gameplanner.game.dto;

import com.gameplanner.game.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GameResponse {

    private long count;
    private String next;
    private String previous;
    private String date;
    private List<GameData> results;


    public GameResponse(Page<Game> games, int page, int size, int year, int month) {
        this.count = games.getTotalElements();
        if (games.hasNext()) {
            this.next = "/api/games/" + String.format("%d/%02d", year, month) + "?page=" + (page + 1) + "&size=" + size;
        }
        if (games.hasPrevious()) {
            this.previous = "/api/games/" + String.format("%d/%02d", year, month) + "?page=" + (page - 1) + "&size=" + size;
        }
        this.date = String.format("%d.%02d", year, month);
        this.results = games.getContent().stream()
                .map(GameData::new)
                .collect(Collectors.toList());

    }


}
