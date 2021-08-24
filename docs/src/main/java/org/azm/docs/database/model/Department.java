package org.azm.docs.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "departments", schema = "docs")
public class Department implements Serializable {

    @Id
    private String code;
    private String description;

}
