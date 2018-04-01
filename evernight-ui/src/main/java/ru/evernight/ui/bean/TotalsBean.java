package ru.evernight.ui.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import ru.evernight.dao.statement.TotalsStatements;
import ru.evernight.dao.statement.TotalsStatements.CategoryTotal;
import ru.evernight.exception.EvernightException;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Named
@ViewScoped
public class TotalsBean implements Serializable {
    @Getter
    @Setter
    private Date from;
    @Getter
    @Setter
    private Date to;
    @Inject
    private TotalsStatements ts;
    @Getter
    private TreeNode root;
    @Getter
    private double total;

    public TotalsBean() {
        clearFilters();
    }

    @PostConstruct
    private void postConstruct() throws EvernightException {
        refreshTotals();
    }

    public void refreshTotals() throws EvernightException {
        CategoryTotal totals = ts.getTotals(from, to);
        root = convert(totals, null);
        total = totals.getTotalPrice();
    }

    public void clearFilters() {
        Calendar cFrom = Calendar.getInstance();
        cFrom.add(Calendar.MONTH, -1);
        from = cFrom.getTime();
        Calendar cTo = DateUtils.round(Calendar.getInstance(), Calendar.DATE);
        cTo.add(Calendar.DATE, 1);
        to = cTo.getTime();
    }

    private TreeNode convert(CategoryTotal totals, TreeNode parent) {
        TreeNode tn = new DefaultTreeNode(totals, parent);
        if (totals.getSubcategories() != null) {
            for (CategoryTotal ctChild : totals.getSubcategories()) {
                convert(ctChild, tn);
            }
        }
        return tn;
    }
}
