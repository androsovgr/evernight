package ru.evernight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@javax.persistence.Table(name = "ent_consumption")
@Data
public class Consumption implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONS_PK")
    private long id;
    @Column(name = "cons_info")
    private String shortInfo;
    @Column(name = "cons_date")
    private Date date;
    @Column(name = "cons_money", columnDefinition = "Decimal(10,0)")
    private double money;
    @Column(name = "cons_comment")
    private String longInfo;
    @Column(name = "cons_type")
    @Enumerated
    private ConsumptionType type;

    @AllArgsConstructor
    public enum ConsumptionType {
        OPERATION("Операционные расходы"), IMPROVMENT("Улучшение");
        @Getter
        private String label;
    }
}
