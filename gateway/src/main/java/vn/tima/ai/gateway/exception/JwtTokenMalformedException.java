package vn.tima.ai.gateway.exception;

public class JwtTokenMalformedException extends Exception {
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
