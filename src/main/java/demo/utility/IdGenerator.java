package demo.utility;

import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {
    private final AtomicLong userSequence = new AtomicLong(100);
    private final AtomicLong complaintSequence = new AtomicLong(1000);
    private final AtomicLong commentSequence = new AtomicLong(5000);

    public long nextUserId() {
        return userSequence.incrementAndGet();
    }

    public long nextComplaintId() {
        return complaintSequence.incrementAndGet();
    }

    public long nextCommentId() {
        return commentSequence.incrementAndGet();
    }
}
