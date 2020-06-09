package rs.interview.backend.web.rest.errors;

public class UsernameAlreadyUsedException extends RuntimeException {

    public UsernameAlreadyUsedException() {
        super("Username name already used!");
    }
}
