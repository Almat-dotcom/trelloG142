package kz.bitlab.trelloG142.exception;

import kz.bitlab.trelloG142.enums.ExceptionStatus;
import lombok.Getter;

@Getter
public class TrelloException extends RuntimeException {
    private ExceptionStatus status;

    public TrelloException(String message, ExceptionStatus status) {
        super(message);
        this.status = status;
    }
}
