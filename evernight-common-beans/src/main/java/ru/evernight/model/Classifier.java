package ru.evernight.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@javax.persistence.Table(name = "ent_classifier")
@Data
@ToString(exclude = {"child", "items"})
public class Classifier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CLAS_PK")
    private long id;
    @Column(name = "CLAS_LABEL")
    private String label;
    @ManyToOne
    @JoinColumn(name = "CLAS_CLAS_PK")
    private Classifier parent;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<Classifier> child;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classifier")
    private List<Item> items;
}
