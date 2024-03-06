package com.gameplanner.game;

public enum GameGenre {
    POINT_AND_CLICK(2, "포인트 앤 클릭"),
    FIGHTING(4, "격투"),
    SHOOTER(5, "슈팅"),
    MUSIC(7, "음악"),
    PLATFORM(8, "플랫폼"),
    PUZZLE(9, "퍼즐"),
    RACING(10, "레이싱"),
    REAL_TIME_STRATEGY(11, "실시간 전략"),
    RPG(12, "롤플레잉"),
    SIMULATOR(13, "시뮬레이션"),
    SPORT(14, "스포츠"),
    STRATEGY(15, "전략"),
    TURN_BASED_STRATEGY(16, "턴제 전략"),
    TACTICAL(24, "전술"),
    HACK_AND_SLASH(25, "핵 앤 슬래시"),
    QUIZ(26, "퀴즈"),
    PINBALL(30, "핀볼"),
    ADVENTURE(31, "어드벤처"),
    INDIE(32, "인디"),
    ARCADE(33, "아케이드"),
    VISUAL_NOVEL(34, "비주얼 노벨"),
    CARD_AND_BOARD_GAME(35, "카드 및 보드 게임"),
    MOBA(36, "MOBA");

    private final int id;
    private final String name;

    GameGenre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static String getNameById(int id) {
        for (GameGenre genre : GameGenre.values()) {
            if (genre.getId() == id) {
                return genre.getName();
            }
        }
        return null;
    }
}