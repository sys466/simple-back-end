package org.azm.deps.database.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "statements", schema = "deps")
public class Statement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "number")
    private String number;
    @Column(name = "type_code")
    private String typeCode;
    @Column(name = "department_code")
    private String departmentCode;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "status_code")
    private Status status;
    @Column(name = "passport")
    private String passport;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "received")
    private LocalDateTime received = LocalDateTime.now();

}
