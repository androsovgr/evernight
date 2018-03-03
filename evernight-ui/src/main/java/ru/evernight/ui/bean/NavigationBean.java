package ru.evernight.ui.bean;

import lombok.Getter;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;
import ru.evernight.model.MenuItem;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class NavigationBean implements Serializable{
    @Inject
    private MenuItemStorageBean misb;
    @Inject
    private LoginBean lb;

    @Getter
    private MenuModel model;
    @Getter
    private int activeTabIndex;

    @PostConstruct
    private void postConstruct() {
        List<MenuItem> items = misb.menuItemsForRole(lb.getUser().getRole());
        model = new DefaultMenuModel();
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest httpR = (HttpServletRequest) ctx.getExternalContext().getRequest();
        String baseUrl = httpR.getServletContext().getContextPath();
        String currentUrl = httpR.getRequestURI();
        int i = 0;
        for (MenuItem mi : items) {
            DefaultMenuItem dmi = new DefaultMenuItem(mi.getLabel());
            dmi.setUrl(baseUrl + "/pages/" + mi.getPath());
            if (currentUrl.endsWith("/" + mi.getPath())) {
                activeTabIndex = i;
            }
            i++;
            model.addElement(dmi);
        }
    }
}
