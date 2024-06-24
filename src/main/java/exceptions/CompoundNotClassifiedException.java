package exceptions;

public class CompoundNotClassifiedException extends Exception {

    public enum ERROR_TYPE {
        NOT_FOUND, BAD_STRUCTURE, IO_EXCEPTION
    }

    private final ERROR_TYPE error_type;

    public CompoundNotClassifiedException(String message, ERROR_TYPE error_type) {
        super(message);
        this.error_type = error_type;
    }

    public ERROR_TYPE getError_type() {
        return error_type;
    }

    @Override
    public String toString() {
        return this.error_type.toString();
    }
}
