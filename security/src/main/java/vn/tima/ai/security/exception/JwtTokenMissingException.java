package vn.tima.ai.security.exception;

public class JwtTokenMissingException extends Exception{
    public JwtTokenMissingException(String message) {
        super(message);
    }
}
