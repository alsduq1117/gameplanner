package com.gameplanner.batch;

import com.gameplanner.client.igdb.IGDBClient;
import com.gameplanner.client.igdb.IGDBGameResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class IGDBItemReader implements ItemReader<IGDBGameResponse> {
    private final IGDBClient igdbClient;
    private int offset = 1000;
    private final int limit = 500; // 한 번의 호출로 가져올 데이터 수
    private List<IGDBGameResponse> igdbGameResponses = new ArrayList<>();

    @Override
    public IGDBGameResponse read() throws Exception {
        if (igdbGameResponses.isEmpty() && offset < 1000) {
            log.info("IGDB 데이터를 가져오기 시작합니다. Offset: {}, Limit: {}", offset, limit);
            igdbGameResponses = igdbClient.getGameList("fields id,name,first_release_date,genres,platforms; limit " + limit + "; offset " + offset + ";" + " sort first_release_date desc;");
            offset += limit;
            if (igdbGameResponses.isEmpty()) {
                log.info("더 이상 가져올 IGDB 데이터가 없습니다.");
                return null; // 모든 데이터를 읽었음을 나타냄
            }
        }

        if (!igdbGameResponses.isEmpty()) {
            return igdbGameResponses.remove(0);
        }
        return null;
    }
}