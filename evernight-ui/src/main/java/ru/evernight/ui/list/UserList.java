package ru.evernight.ui.list;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.evernight.dao.statement.ListAndCount;
import ru.evernight.dao.statement.UserStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.AccountStatus;
import ru.evernight.model.User;
import ru.evernight.model.UserRole;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Slf4j
@ViewScoped
@Named
public class UserList extends LazyDataModel<User> {
    @Inject
    private UserStatements us;

    @Getter
    @Setter
    private User selected;
    @Getter
    private User forCreate;

    @SneakyThrows(EvernightException.class)
    @Override
    public List<User> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        ListAndCount<User> result = us.lazyList(0, 10);
        setRowCount((int) result.getTotalCount());
        if (!result.getElements().contains(selected) && !result.getElements().isEmpty()) {
            selected = result.getElements().get(0);
        }
        return result.getElements();
    }

    public void prepareCreate() {
        forCreate = new User();
        forCreate.setRole(UserRole.WAITER);
        forCreate.setStatus(AccountStatus.ACTIVE);
    }

    public void create() throws EvernightException {
        us.create(forCreate);
    }

    public void prepareModify() {
        selected.setPasswordHash("");
    }

    public void modify() throws EvernightException {
        us.update(selected);
    }

    public void delete() throws EvernightException {
        us.delete(selected.getId());
    }

    @Override
    public Object getRowKey(User object) {
        return object.getId();
    }

    @SneakyThrows(EvernightException.class)
    @Override
    public User getRowData(String rowKey) {
        return us.byId(Long.parseLong(rowKey));
    }
}
