package com.gameplanner.client.opencritic;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "opencritic-api", url = "https://opencritic-api.p.rapidapi.com", configuration = OpenCriticFeignConfig.class)
public interface OpenCriticClient {

    @GetMapping("/game/search")
    OpenCriticSearchGameResponse searchGame(@RequestParam("criteria") String criteria);

    @GetMapping("/game")
    List<OpenCriticGameResponse> getGameList(@RequestParam("platforms") String platforms, @RequestParam("sort") String sort);

}
