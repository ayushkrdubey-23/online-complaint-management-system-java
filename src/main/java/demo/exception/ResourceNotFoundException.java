package demo.exception;

// Using standard Java Runtime Exception to keep it error-free regardless of local Maven sync status
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}