package chapter16.localization;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatTest {

    @Test
    void testFormatMethod() {
        LocalDate date = LocalDate.of(2020, Month.OCTOBER, 3);
        LocalTime time = LocalTime.of(16, 12, 34);
        LocalDateTime dt = LocalDateTime.of(date, time);

        System.out.println(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println(time.format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.out.println(dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        var formatter = DateTimeFormatter.ofPattern("MM dd, yyyy 'at' hh:mm");
        System.out.println(dt.format(formatter));
    }


}
