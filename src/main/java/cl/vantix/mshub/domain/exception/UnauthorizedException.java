package cl.vantix.mshub.domain.exception;
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) { super(message); }
}
