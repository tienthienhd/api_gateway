package vn.tima.ai.gateway.exception;

public class JwtTokenMissingException extends Exception {
    public JwtTokenMissingException(String message) {
        super(message);
    }
}
