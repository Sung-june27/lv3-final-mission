package finalmission.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import finalmission.global.error.exception.InternalServerException;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class HolidayRestClient {

    private final RestClient restClient;
    private final String baseUrl;
    private final String serviceKey;
    private final ObjectMapper objectMapper;

    public List<HolidayResponse> getHolidayByMonth(LocalDate date) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("_type", "json")
                .queryParam("solYear", date.getYear())
                .queryParam("solMonth", String.format("%02d", date.getMonthValue()))
                .queryParam("ServiceKey", serviceKey)
                .build()
                .toUri();

        String response = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);

        try {
            return mappingToResponse(response);
        } catch (JsonProcessingException e) {
            throw new InternalServerException("공휴일 외부 API 응답 매핑 예외입니다.");
        }
    }

    private List<HolidayResponse> mappingToResponse(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        JsonNode item = jsonNode.get("response").get("body").get("items").get("item");

        if (item.isObject()) {
            return List.of(toHolidayResponse(item));
        }

        return toHolidayResponses(item);
    }

    private List<HolidayResponse> toHolidayResponses(JsonNode item) throws JsonProcessingException {
        List<HolidayResponse> responses = new ArrayList<>();
        Iterator<JsonNode> items = item.elements();
        while (items.hasNext()) {
            HolidayResponse vo = toHolidayResponse(items.next());
            responses.add(vo);
        }
        return responses;
    }

    private HolidayResponse toHolidayResponse(JsonNode item) throws JsonProcessingException {
        return objectMapper.treeToValue(item, HolidayResponse.class);
    }
}
