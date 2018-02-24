package ru.evernight.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ent_user")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USR_PK")
    private long id;
    @Column(name = "USR_EMAIL")
    private String email;
    @Column(name = "USR_PASSWORD_HASH")
    private String passwordHash;
    @Column(name = "USR_SURNAME")
    private String surname;
    @Column(name = "USR_NAME")
    private String name;
    @Column(name = "USR_MIDDLENAME")
    private String middlename;

}
