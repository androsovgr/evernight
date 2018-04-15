package ru.evernight.ui.bean;

import ru.evernight.model.MenuItem;
import ru.evernight.model.UserRole;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@ApplicationScoped
@Named
public class MenuItemStorageBean {
    private Map<UserRole, List<MenuItem>> access;
    @Inject
    private LoginBean lb;

    @PostConstruct
    private void postConstruct() {
        access = new HashMap<>();
        MenuItem hallMi = new MenuItem("Зал", "hall.jsf");
        MenuItem usersMi = new MenuItem("Пользователи", "users.jsf");
        MenuItem totalsMi = new MenuItem("Денежные итоги", "totals.jsf");
        MenuItem ordersMi = new MenuItem("Заказы", "orders.jsf");
        List<MenuItem> waiterGrants = new ArrayList<>();
        waiterGrants.add(hallMi);
        ArrayList<MenuItem> managerGrants = new ArrayList<>(waiterGrants);
        managerGrants.add(usersMi);
        managerGrants.add(totalsMi);
        managerGrants.add(ordersMi);
        ArrayList<MenuItem> adminGrants = new ArrayList<>(managerGrants);
        access.put(UserRole.WAITER, Collections.unmodifiableList(waiterGrants));
        access.put(UserRole.MANAGER, Collections.unmodifiableList(managerGrants));
        access.put(UserRole.ADMIN, Collections.unmodifiableList(adminGrants));
    }

    public List<MenuItem> menuItemsForRole(UserRole ur) {
        return access.get(ur);
    }

    public boolean urlAvailable(String url) {
        List<MenuItem> availableItems = access.get(lb.getUser().getRole());
        for (MenuItem mi : availableItems) {
            if (url.endsWith("/" + mi.getPath())) {
                return true;
            }
        }
        return false;
    }
}
