package demo.controller;

import demo.dto.*;
import demo.security.AuthHelper;
import demo.security.SessionManager.Session;
import demo.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final AuthHelper authHelper;

    public ComplaintController(ComplaintService complaintService, AuthHelper authHelper) {
        this.complaintService = complaintService;
        this.authHelper = authHelper;
    }

    @PostMapping
    public ResponseEntity<ComplaintResponse> createComplaint(
            @RequestHeader("X-Auth") String token,
            @RequestBody ComplaintRequest req) {
        Session session = authHelper.validateAndGetSession(token);
        return ResponseEntity.ok(complaintService.createComplaint(session.getUserId(), req));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ComplaintResponse>> getMyComplaints(@RequestHeader("X-Auth") String token) {
        Session session = authHelper.validateAndGetSession(token);
        return ResponseEntity.ok(complaintService.getUserComplaints(session.getUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getComplaintById(
            @RequestHeader("X-Auth") String token,
            @PathVariable long id) {
        authHelper.validateAndGetSession(token);
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ComplaintResponse> addComment(
            @RequestHeader("X-Auth") String token,
            @PathVariable long id,
            @RequestBody CommentRequest req) {
        Session session = authHelper.validateAndGetSession(token);
        return ResponseEntity.ok(complaintService.addComment(id, session.getUserId(), req));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<ComplaintResponse> assignComplaint(
            @RequestHeader("X-Auth") String token,
            @PathVariable long id,
            @RequestBody AssignRequest req) {
        Session session = authHelper.validateAndGetSession(token);
        authHelper.verifyAdminRole(session);
        return ResponseEntity.ok(complaintService.assignComplaint(id, req));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> changeStatus(
            @RequestHeader("X-Auth") String token,
            @PathVariable long id,
            @RequestBody StatusRequest req) {
        Session session = authHelper.validateAndGetSession(token);
        return ResponseEntity.ok(complaintService.changeStatus(id, session.getUserId(), session.getRole(), req));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ComplaintResponse>> searchComplaints(
            @RequestHeader("X-Auth") String token,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q) {
        Session session = authHelper.validateAndGetSession(token);
        authHelper.verifyAdminRole(session);
        return ResponseEntity.ok(complaintService.searchComplaints(status, category, q));
    }
}
