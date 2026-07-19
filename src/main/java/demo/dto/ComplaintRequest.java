package demo.dto;

import demo.model.ComplaintCategory;
import demo.model.ComplaintPriority;

public class ComplaintRequest {
    private String title;
    private String description;
    private ComplaintCategory category;
    private ComplaintPriority priority;

    public ComplaintRequest() {}

    public ComplaintRequest(String title, String description, ComplaintCategory category, ComplaintPriority priority) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
    }

    public String getTitle() { 
        return title; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getDescription() { 
        return description; 
    }
    
    public void setDescription(String description) { 
        this.description = description; 
    }

    public ComplaintCategory getCategory() { 
        return category; 
    }
    
    public void setCategory(ComplaintCategory category) { 
        this.category = category; 
    }

    public ComplaintPriority getPriority() { 
        return priority; 
    }
    
    public void setPriority(ComplaintPriority priority) { 
        this.priority = priority; 
    }
}
