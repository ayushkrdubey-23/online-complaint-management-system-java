package demo.model;

import java.time.Instant;

public class Comment {
    private long id;
    private long complaintId;
    private long authorId;
    private String text;
    private Instant createdAt;

    public Comment() {}

    public Comment(long id, long complaintId, long authorId, String text) {
        this.id = id;
        this.complaintId = complaintId;
        this.authorId = authorId;
        this.text = text;
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getComplaintId() { return complaintId; }
    public void setComplaintId(long complaintId) { this.complaintId = complaintId; }

    public long getAuthorId() { return authorId; }
    public void setAuthorId(long authorId) { this.authorId = authorId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
