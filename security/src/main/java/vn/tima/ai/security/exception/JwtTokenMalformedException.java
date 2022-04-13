package vn.tima.ai.security.exception;

public class JwtTokenMalformedException extends Exception{
    public JwtTokenMalformedException(String message) {
        super(message);
    }
}
