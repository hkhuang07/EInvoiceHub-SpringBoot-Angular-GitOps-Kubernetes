package vn.softz.app.einvoicehub.exception;

public class InvalidDataException extends BusinessException {

    public InvalidDataException(String message) {
        super("invalid.data", message);
    }

    public InvalidDataException(String code, String message) {
        super(code, message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
