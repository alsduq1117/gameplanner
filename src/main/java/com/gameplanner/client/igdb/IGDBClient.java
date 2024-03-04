package com.gameplanner.client.igdb;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "IGDB-client", url = "https://api.igdb.com/v4", configuration = IGDBFeignConfig.class)
public interface IGDBClient {

    @PostMapping("/games")
    List<IGDBGameResponse> getGameList(@RequestBody String body);

    @PostMapping("/platforms")
    List<IGDBPlatformResponse> getPlatForms(@RequestBody String body);






}

