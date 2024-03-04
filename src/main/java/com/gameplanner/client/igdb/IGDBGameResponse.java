package com.gameplanner.client.igdb;

import lombok.Data;

import java.util.List;

@Data
public class IGDBGameResponse {
    private Long id; // 게임 ID
    private String name; // 게임 이름
    private int category; // 게임 카테고리
    private int cover; // 게임 커버 이미지
    private long first_release_date; // 게임 첫 출시 일자 (timestamp 형식)
    private List<Integer> genres; // 게임 장르 목록
    private List<Long> platforms; // 게임 플랫폼 목록
    private List<Integer> screenshots; // 게임 스크린샷 목록
    private List<Integer> videos; // 게임 영상 목록
    private List<Long> language_supports; // 게임 언어 지원 목록
}

