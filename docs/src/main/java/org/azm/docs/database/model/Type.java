package org.azm.docs.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "types", schema = "docs")
public class Type implements Serializable {

    @Id
    private String code;
    private String description;

}
