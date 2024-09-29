package com.gameplanner.client.steam;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "steam-api", url = "https://store.steampowered.com")
public interface SteamClient {

    @GetMapping("/ISteamApps/GetAppList/v0002/")
    SteamAppListResponse getAppList(@RequestParam("key") String key, @RequestParam("format") String format);

    @GetMapping("/api/appdetails/")
    SteamAppDetailsResponse getAppDetails(@RequestParam("appids") String appids);

}
