package es.jguimar.tinybankAPI.infrastructure.exception;

public class ResourceExistsException extends Exception {

    public ResourceExistsException() {
        super();
    }

    public ResourceExistsException(String message) {
        super(message);
    }

}
