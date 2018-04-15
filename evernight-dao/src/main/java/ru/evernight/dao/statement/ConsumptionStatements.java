package ru.evernight.dao.statement;

import ru.evernight.model.Consumption;

import javax.ejb.Stateless;

@Stateless
public class ConsumptionStatements extends CrudStatements<Consumption> {
    public ConsumptionStatements() {
        super(Consumption.class);
    }
}
