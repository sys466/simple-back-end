package org.azm.docs.exception;

import lombok.Getter;

import java.util.List;

public class IncorrectDataException extends RuntimeException {

    @Getter
    private final List<String> incorrectFields;

    public IncorrectDataException(String errorMessage, List<String> incorrectFields) {
        super(errorMessage);
        this.incorrectFields = incorrectFields;
    }

}
