package rs.interview.backend.web.rest.errors;

public class EntityForbiddenException extends RuntimeException {
    public EntityForbiddenException(String message) {
        super(message);
    }
}
