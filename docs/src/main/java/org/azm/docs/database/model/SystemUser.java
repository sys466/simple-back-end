package org.azm.docs.database.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "docs")
public class SystemUser implements Serializable {

    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "hash_password")
    private String password;
    @Column(name = "full_name")
    private String fullName;

    public SystemUser(String username) {
        this.username = username;
    }

}
