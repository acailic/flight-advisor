package rs.interview.backend.web.rest.errors;

public class UserResourceException extends RuntimeException {
    public UserResourceException(String message) {
        super(message);
    }
}