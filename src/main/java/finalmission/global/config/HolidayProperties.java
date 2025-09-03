package finalmission.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "holiday")
public record HolidayProperties(
    String serviceKey,
    String baseUrl
) {
}
