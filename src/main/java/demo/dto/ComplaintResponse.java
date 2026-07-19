package demo.dto;

import demo.model.ComplaintCategory;
import demo.model.ComplaintPriority;
import demo.model.ComplaintStatus;
import java.time.Instant;
import java.util.List;

public class ComplaintResponse {
    private long id;
    private long userId;
    private String title;
    private String description;
    private ComplaintCategory category;
    private ComplaintPriority priority;
    private ComplaintStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Long assignee;
    private List<CommentResponse> comments;

    public ComplaintResponse() {}

    public ComplaintResponse(long id, long userId, String title, String description,
                             ComplaintCategory category, ComplaintPriority priority, ComplaintStatus status,
                             Instant createdAt, Instant updatedAt, Long assignee, List<CommentResponse> comments) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.assignee = assignee;
        this.comments = comments;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ComplaintCategory getCategory() { return category; }
    public void setCategory(ComplaintCategory category) { this.category = category; }

    public ComplaintPriority getPriority() { return priority; }
    public void setPriority(ComplaintPriority priority) { this.priority = priority; }

    public ComplaintStatus getStatus() { return status; }
    public void setStatus(ComplaintStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Long getAssignee() { return assignee; }
    public void setAssignee(Long assignee) { this.assignee = assignee; }

    public List<CommentResponse> getComments() { return comments; }
    public void setComments(List<CommentResponse> comments) { this.comments = comments; }
}