package demo.dto;

import java.time.Instant;

public class CommentResponse {
    private long id;
    private long authorId;
    private String authorName;
    private String text;
    private Instant createdAt;

    public CommentResponse() {}
    public CommentResponse(long id, long authorId, String authorName, String text, Instant createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.text = text;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getAuthorId() { return authorId; }
    public void setAuthorId(long authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
