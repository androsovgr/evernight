package ru.evernight.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "ent_order_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORIT_PK")
    private long id;
    @Column(name = "ORIT_TIME")
    private Date time;
    @ManyToOne
    @JoinColumn(name = "ORIT_ITEM_PK")
    private Item item;
    @JoinColumn(name = "ORIT_ORD_PK")
    @ManyToOne
    private Order order;
}
