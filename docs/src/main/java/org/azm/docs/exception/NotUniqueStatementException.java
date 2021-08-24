package org.azm.docs.exception;

public class NotUniqueStatementException extends RuntimeException {

    public NotUniqueStatementException(String errorMessage) {
        super(errorMessage);
    }

}
