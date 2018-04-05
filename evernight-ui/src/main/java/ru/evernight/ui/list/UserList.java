package ru.evernight.ui.list;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.statement.UserStatements;
import ru.evernight.model.AccountStatus;
import ru.evernight.model.User;
import ru.evernight.model.UserRole;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@ViewScoped
@Named
public class UserList extends ExtendedLazyDataModel<User> {
    @Inject
    @Getter(AccessLevel.PROTECTED)
    private UserStatements mainStatements;

    public void prepareCreate() {
        forCreate = new User();
        forCreate.setRole(UserRole.WAITER);
        forCreate.setStatus(AccountStatus.ACTIVE);
    }

    public void prepareModify() {
        selected.setPasswordHash("");
    }

}
