package ru.evernight.dao.statement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Classifier;
import ru.evernight.model.Consumption;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Stateless
public class TotalsStatements {
    @PersistenceContext(unitName = "evernight")
    protected EntityManager em;

    public CategoryTotal getTotals(Date from, Date to) throws EvernightException {
        try {
            log.info("Requested totals from {} to {}", from, to);
            Classifier rootProductClassifier = em.find(Classifier.class, 1L);
            Query q = em.createNativeQuery("select i.ITEM_LABEL,i.ITEM_CLAS_PK, count(1) as totalCount, sum(i.ITEM_PRICE) as totalPrice from ent_item i " +
                    "join ent_order_item oi on i.ITEM_PK=oi.ORIT_ITEM_PK " +
                    "join ent_order o on oi.ORIT_ORD_PK=o.ORD_PK " +
                    "where o.ORD_CLOSED>?1 and o.ORD_CLOSED<?2 " +
                    "group by i.ITEM_PK");
            List<Object[]> productTotalsObjects = q.setParameter(1, from).setParameter(2, to).getResultList();
            Map<Long, List<ProductTotal>> productTotals;
            if (productTotalsObjects.size() == 0 || productTotalsObjects.get(0)[0] == null) {
                productTotals = new HashMap<>();
            } else {
                productTotals = productTotalsObjects.stream().map(o -> new ProductTotal((String) o[0], ((BigInteger) o[1]).longValue(), ((BigInteger) o[2]).intValue(), ((BigDecimal) o[3]).doubleValue())).collect(Collectors.groupingBy(ProductTotal::getClassId));
            }
            CategoryTotal result = convert(rootProductClassifier, productTotals);
            addConsumptions(result, from, to);
            log.debug("Got totals: {}", result);
            return result;
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка взаимодействия с БД", e);
        }
    }

    private void addConsumptions(CategoryTotal result, Date from, Date to) {
        Query q = em.createNativeQuery("select c.cons_type, count(1), sum(c.cons_money) from ent_consumption c " +
                "where c.cons_date>=?1 and c.cons_date<=?2 " +
                "group by (cons_type) ");
        List<Object[]> consumptionTotalsObjects = q.setParameter(1, from).setParameter(2, to).getResultList();
        for (Object[] cto : consumptionTotalsObjects) {
            Consumption.ConsumptionType type = Consumption.ConsumptionType.values()[((int) cto[0])];
            CategoryTotal ct = new CategoryTotal(type.getLabel(), ((BigInteger) cto[1]).longValue(), -((BigDecimal) cto[2]).doubleValue(), Collections.emptyList());
            result.getSubcategories().add(ct);
            result.totalPrice += ct.totalPrice;
        }
    }

    private CategoryTotal convert(Classifier classifier, Map<Long, List<ProductTotal>> productTotals) {
        CategoryTotal ct = new CategoryTotal();
        ct.setLabel(classifier.getLabel());
        ct.setSubcategories(new ArrayList<>());
        for (Classifier cChidl : classifier.getChild()) {
            ct.getSubcategories().add(convert(cChidl, productTotals));

        }
        if (productTotals.get(classifier.getId()) != null) {
            for (ProductTotal pt : productTotals.get(classifier.getId())) {
                CategoryTotal ctChild = new CategoryTotal();
                ctChild.setTotalCount(pt.totalCount);
                ctChild.setTotalPrice(pt.totalPrice);
                ctChild.setLabel(pt.label);
                ct.getSubcategories().add(ctChild);
            }
        }
        for (CategoryTotal ctChild : ct.getSubcategories()) {
            ct.totalCount += ctChild.totalCount;
            ct.totalPrice += ctChild.totalPrice;
        }
        return ct;
    }

    @AllArgsConstructor
    @Data
    public static class ProductTotal {
        private String label;
        private long classId;
        private int totalCount;
        private double totalPrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryTotal implements Serializable {
        private String label;
        private long totalCount;
        private double totalPrice;
        private List<CategoryTotal> subcategories;
    }
}
