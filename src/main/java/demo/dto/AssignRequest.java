package demo.dto;

public class AssignRequest {
    private Long assigneeId;

    public AssignRequest() {}
    
    public AssignRequest(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getAssigneeId() { 
        return assigneeId; 
    }
    
    public void setAssigneeId(Long assigneeId) { 
        this.assigneeId = assigneeId; 
    }
}