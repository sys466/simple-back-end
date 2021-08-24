package org.azm.docs.exception;

public class NoSuchStatementException extends RuntimeException {

    public NoSuchStatementException(String errorMessage) {
        super(errorMessage);
    }

}
