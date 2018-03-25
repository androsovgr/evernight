package ru.evernight.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterJoinTable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@javax.persistence.Table(name = "ent_table")
@FilterDef(name = "activeOrders")
@Data
@ToString(exclude = "orders")
@EqualsAndHashCode(exclude = "orders")
public class Table implements Serializable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TBL_PK")
    private long id;
    @Column(name = "TBL_LABEL")
    private String label;
    @OneToMany(mappedBy = "table")
    @Filter(name = "activeOrders",condition = "ORD_CLOSED IS NULL")
    private List<Order> orders;

}
