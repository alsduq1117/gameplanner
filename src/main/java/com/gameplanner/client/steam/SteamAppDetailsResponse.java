package com.gameplanner.client.steam;

import lombok.Data;

import java.util.Map;

@Data
public class SteamAppDetailsResponse {

    private AppDetails data;

    @Data
    public static class AppDetails {
        private boolean success;
        private GameDetails game;
    }

    @Data
    public static class GameDetails {
        private String type;
        private String name;
        private int steam_appid;
        private String required_age;
        private boolean is_free;
        private String controller_support;
        private int[] dlc;
    }

}
