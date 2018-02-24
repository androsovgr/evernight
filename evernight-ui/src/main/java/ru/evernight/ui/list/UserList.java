package ru.evernight.ui.list;

import org.primefaces.model.LazyDataModel;
import ru.evernight.dao.statement.UserStatements;
import ru.evernight.model.User;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@ViewScoped
@Named
public class UserList extends LazyDataModel {
    @Inject
    private UserStatements us;

    public void click() {
        List<User> result = us.allUsers();
        System.out.println(result);
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(result + ""));
    }
}
