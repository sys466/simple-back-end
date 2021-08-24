package org.azm.docs.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatementExportDTO implements Serializable {

    private int id;
    private String number;
    private String typeCode;
    private String departmentCode;
    private String statusCode;
    private String passport;
    private String fullName;
    private String systemUserUsername;

}
