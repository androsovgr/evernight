package ru.evernight.model;

import lombok.*;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items;

    public double total() {
        if (items == null || items.isEmpty()) {
            return 0;
        }
        return items.stream().map(i -> i.getItem().getPrice()).reduce((d1, d2) -> d1 + d2).get();
    }

    public List<GroupWithCount> grouppedItems() {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }
        return items.stream().collect(Collectors.groupingBy(OrderItem::getItem, Collectors.counting()))
                .entrySet().stream().map(e -> new GroupWithCount(e.getKey(), e.getValue().intValue()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    public static class GroupWithCount {
        private Item item;
        private int count;
    }
}
