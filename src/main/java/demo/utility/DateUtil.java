package demo.utility;

import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class DateUtil {
    public Instant getCurrentInstant() {
        return Instant.now();
    }
}
