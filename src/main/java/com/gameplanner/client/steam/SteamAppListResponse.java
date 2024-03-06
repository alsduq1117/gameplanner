package com.gameplanner.client.steam;

import lombok.*;

import java.util.List;

@Data
public class SteamAppListResponse {

    private AppList applist;

    @Data
    public static class AppList {
        private List<Game> apps;
    }

    @Data
    public static class Game {
        private int appid;
        private String name;
    }

}

