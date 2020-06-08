package rs.interview.backend.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "localhost:8080";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/error");
    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    private ErrorConstants() {
    }
}
