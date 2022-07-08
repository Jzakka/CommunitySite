package CommunitySIte.demo.web.formatter;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class DateFormatterTest {

    DateFormatter formatter = new DateFormatter();

    @Test
    void print() {
        LocalDateTime now = LocalDateTime.now();

        String result = formatter.print(now.withNano(0), Locale.KOREA);
        System.out.println("result = " + result);
    }
}