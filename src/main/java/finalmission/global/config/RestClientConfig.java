package finalmission.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import finalmission.external.HolidayRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(HolidayProperties.class)
@RequiredArgsConstructor
public class RestClientConfig {

    private final HolidayProperties holidayProperties;

    @Bean
    public HolidayRestClient holidayRestClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return new HolidayRestClient(
                restClientBuilder().build(),
                holidayProperties.baseUrl(),
                holidayProperties.serviceKey(),
                objectMapper
        );
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
