package finalmission.external;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private static final String LOCDATE_FORMAT = "yyyyMMdd";

    private final HolidayRestClient holidayRestClient;

    public boolean isHoliday(LocalDate date) {
        List<HolidayResponse> holidayResponses = holidayRestClient.getHolidayByMonth(date);

        String formattedDate = formatDate(date);
        return holidayResponses.stream()
                .anyMatch(holidayResponse -> holidayResponse.isSameDate(formattedDate));
    }

    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCDATE_FORMAT);
        return formatter.format(date);
    }
}
