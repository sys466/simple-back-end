package org.azm.docs.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatementDTO implements Serializable {

    private String number;
    private String typeCode;
    private String departmentCode;
    private String passport;
    private String fullName;

}
