package tech.ada.java.cursospring.api.exception;

import org.springframework.http.HttpStatus;

public class AmizadeInvalidaBusinessException extends AbstractException {

    public AmizadeInvalidaBusinessException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
