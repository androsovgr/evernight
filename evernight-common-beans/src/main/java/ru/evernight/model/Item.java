package ru.evernight.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@javax.persistence.Table(name = "ent_item")
@Data
public class Item implements Serializable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_PK")
    private long id;
    @Column(name = "ITEM_LABEL")
    private String label;
    @ManyToOne
    @JoinColumn(name = "ITEM_CLAS_PK")
    private Classifier classifier;
    @Column(name = "ITEM_PRICE", columnDefinition = "Decimal(10,0)")
    private double price;
    @Column(name = "ITEM_TYPE")
    @Enumerated
    private ItemType itemType;
    @Column(name = "ITEM_ACTIVE")
    @Enumerated
    private ActiveStatus active;

    public enum ItemType {
        MAIN_DISH, OPTION
    }
}
