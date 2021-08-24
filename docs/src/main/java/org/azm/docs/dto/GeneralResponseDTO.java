package org.azm.docs.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
public class GeneralResponseDTO implements Serializable {

    private String timestamp;
    private HttpStatus httpStatus;
    private String message;
    private List<Object> data;

    public GeneralResponseDTO(HttpStatus httpStatus, String message) {
        this.timestamp = LocalDateTime.now().toString();
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = Collections.emptyList();
    }

    public GeneralResponseDTO(HttpStatus httpStatus, String message, Object data) {
        this.timestamp = LocalDateTime.now().toString();
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = List.of(data);
    }

    public GeneralResponseDTO(HttpStatus httpStatus, String message, List<Object> data) {
        this.timestamp = LocalDateTime.now().toString();
        this.httpStatus = httpStatus;
        this.message = message;
        this.data = data;
    }

}
