package finalmission.service;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.external.HolidayService;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @DisplayName("공휴일인지 확인한다.")
    @Test
    void isHoliday() {
        // given
        LocalDate date = LocalDate.of(2020, 6, 6);

        // when
        boolean isHoliday = holidayService.isHoliday(date);

        // then
        assertThat(isHoliday).isTrue();
    }
}
