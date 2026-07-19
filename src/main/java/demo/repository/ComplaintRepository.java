package demo.repository;

import demo.model.Comment;
import demo.model.Complaint;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ComplaintRepository {
    private final Map<Long, Complaint> complaints = new ConcurrentHashMap<>();
    private final Map<Long, List<Comment>> comments = new ConcurrentHashMap<>();

    public Complaint save(Complaint complaint) {
        complaints.put(complaint.getId(), complaint);
        comments.computeIfAbsent(complaint.getId(), k -> new ArrayList<>());
        return complaint;
    }

    public Optional<Complaint> findById(long id) {
        return Optional.ofNullable(complaints.get(id));
    }

    public Collection<Complaint> findAll() {
        return complaints.values();
    }

    public List<Comment> findCommentsByComplaintId(long complaintId) {
        return comments.getOrDefault(complaintId, new ArrayList<>());
    }

    public void addComment(long complaintId, Comment comment) {
        comments.computeIfAbsent(complaintId, k -> new ArrayList<>()).add(comment);
    }
}