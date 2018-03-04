package ru.evernight.model.aggregative;

import lombok.Data;
import org.hibernate.annotations.Where;
import ru.evernight.model.Identifiable;
import ru.evernight.model.Order;
import ru.evernight.model.Table;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@javax.persistence.Table(name = "ent_order")
@Data
public class AggregatedOrder implements Identifiable,Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORD_PK")
    private long id;
    @JoinColumn(name = "ORD_PK")
    @OneToOne(fetch = FetchType.EAGER)
    private Order order;

}
