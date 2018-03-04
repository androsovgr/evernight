package ru.evernight.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@javax.persistence.Table(name = "ent_table")
@Data
public class Table implements Serializable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TBL_PK")
    private long id;
    @Column(name = "TBL_LABEL")
    private String label;

}
