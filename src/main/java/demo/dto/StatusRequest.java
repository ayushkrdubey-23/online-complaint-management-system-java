package demo.dto;

import demo.model.ComplaintStatus;

public class StatusRequest {
    private ComplaintStatus status;

    public StatusRequest() {}
    
    public StatusRequest(ComplaintStatus status) {
        this.status = status;
    }

    public ComplaintStatus getStatus() { 
        return status; 
    }
    
    public void setStatus(ComplaintStatus status) { 
        this.status = status; 
    }
}
