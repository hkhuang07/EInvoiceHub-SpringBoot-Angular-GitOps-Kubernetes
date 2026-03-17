package vn.softz.app.einvoicehub.exception;

public class UnAuthorizedException extends BusinessException {

    public UnAuthorizedException() {
        super("auth.unauthorized", "Unauthorized: missing or invalid authentication context");
    }

    public UnAuthorizedException(String message) {
        super("auth.unauthorized", message);
    }

    public UnAuthorizedException(String code, String message) {
        super(code, message);
    }
}
