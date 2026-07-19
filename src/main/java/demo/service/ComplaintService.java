package demo.service;

import demo.dto.*;
import demo.model.*;
import demo.repository.ComplaintRepository;
import demo.repository.UserRepository;
import demo.utility.IdGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final IdGenerator idGenerator;

    public ComplaintService(ComplaintRepository complaintRepository, UserRepository userRepository, IdGenerator idGenerator) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.idGenerator = idGenerator;
    }

    public ComplaintResponse createComplaint(long userId, ComplaintRequest req) {
        if (req == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body cannot be null");
        }
        if (req.getTitle() == null || req.getTitle().trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must be at least 3 characters long");
        }
        if (req.getDescription() == null || req.getDescription().trim().length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must be at least 10 characters long");
        }
        if (req.getCategory() == null || req.getPriority() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category and Priority are required");
        }

        long id = idGenerator.nextComplaintId();
        
        // Explicitly ensuring system defaults are applied before saving to memory maps
        Complaint complaint = new Complaint(id, userId, req.getTitle(), req.getDescription(), req.getCategory(), req.getPriority());
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setCreatedAt(Instant.now());
        complaint.setUpdatedAt(Instant.now());
        
        complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }

    /*public ComplaintResponse createComplaint(long userId, ComplaintRequest req) {
        if (req.getTitle() == null || req.getTitle().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title must be at least 3 characters long");
        }
        if (req.getDescription() == null || req.getDescription().length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must be at least 10 characters long");
        }
        if (req.getCategory() == null || req.getPriority() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category and Priority are required");
        }

        long id = idGenerator.nextComplaintId();
        Complaint complaint = new Complaint(id, userId, req.getTitle(), req.getDescription(), req.getCategory(), req.getPriority());
        complaintRepository.save(complaint);
        return mapToResponse(complaint);
    }*/

    public ComplaintResponse getComplaintById(long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));
        return mapToResponse(complaint);
    }

    public List<ComplaintResponse> getUserComplaints(long userId) {
        return complaintRepository.findAll().stream()
                .filter(c -> c.getUserId() == userId)
                .sorted((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> searchComplaints(String status, String category, String query) {
        return complaintRepository.findAll().stream()
                .filter(c -> status == null || c.getStatus().name().equalsIgnoreCase(status))
                .filter(c -> category == null || c.getCategory().name().equalsIgnoreCase(category))
                .filter(c -> query == null || c.getTitle().toLowerCase().contains(query.toLowerCase()) 
                        || c.getDescription().toLowerCase().contains(query.toLowerCase()))
                .sorted((c1, c2) -> c1.getCreatedAt().compareTo(c2.getCreatedAt()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse addComment(long complaintId, long authorId, CommentRequest req) {
        if (req.getText() == null || req.getText().length() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment text must be at least 2 characters long");
        }

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));

        long commentId = idGenerator.nextCommentId();
        Comment comment = new Comment(commentId, complaintId, authorId, req.getText());
        complaintRepository.addComment(complaintId, comment);
        
        complaint.setUpdatedAt(Instant.now());
        complaintRepository.save(complaint);
        
        return mapToResponse(complaint);
    }

    public ComplaintResponse assignComplaint(long complaintId, AssignRequest req) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));

        User targetUser = userRepository.findById(req.getAssigneeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignee user not found"));

        if (targetUser.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assignee must be an administrator");
        }

        complaint.setAssignee(targetUser.getId());
        complaint.setStatus(ComplaintStatus.IN_PROGRESS);
        complaint.setUpdatedAt(Instant.now());
        complaintRepository.save(complaint);

        return mapToResponse(complaint);
    }

    public ComplaintResponse changeStatus(long complaintId, long userId, Role userRole, StatusRequest req) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint not found"));

        if (userRole == Role.ADMIN) {
            if (req.getStatus() == ComplaintStatus.OPEN) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot revert status back to OPEN");
            }
            complaint.setStatus(req.getStatus());
        } else {
            if (complaint.getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: You do not own this complaint");
            }
            if (req.getStatus() != ComplaintStatus.CLOSED || complaint.getStatus() != ComplaintStatus.RESOLVED) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users can only change status to CLOSED on RESOLVED complaints");
            }
            complaint.setStatus(ComplaintStatus.CLOSED);
        }

        complaint.setUpdatedAt(Instant.now());
        complaintRepository.save(complaint);
        
        return mapToResponse(complaint);
    }

    private ComplaintResponse mapToResponse(Complaint c) {
        List<CommentResponse> mappedComments = complaintRepository.findCommentsByComplaintId(c.getId()).stream()
                .sorted((cm1, cm2) -> cm1.getCreatedAt().compareTo(cm2.getCreatedAt()))
                .map(cm -> {
                    String authorName = userRepository.findById(cm.getAuthorId())
                            .map(u -> u.getName())
                            .orElse("Unknown");
                    return new CommentResponse(cm.getId(), cm.getAuthorId(), authorName, cm.getText(), cm.getCreatedAt());
                })
                .collect(Collectors.toList());

        return new ComplaintResponse(
                c.getId(), c.getUserId(), c.getTitle(), c.getDescription(),
                c.getCategory(), c.getPriority(), c.getStatus(),
                c.getCreatedAt(), c.getUpdatedAt(), c.getAssignee(), mappedComments
        );
    }
}