package ru.evernight.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@javax.persistence.Table(name = "ent_order")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "items")
public class Order implements Serializable, Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORD_PK")
    private long id;
    @JoinColumn(name = "ORD_TBL_PK")
    @ManyToOne(fetch = FetchType.EAGER)
    private Table table;
    @JoinColumn(name = "ORD_WAITER_USR_PK")
    @ManyToOne(fetch = FetchType.EAGER)
    private User waiter;
    @Column(name = "ORD_OPENED")
    private Date openTime;
    @Column(name = "ORD_CLOSED")
    private Date closeTime;
    @Column(name = "ORD_COMMENT")
    private String comment;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

}
