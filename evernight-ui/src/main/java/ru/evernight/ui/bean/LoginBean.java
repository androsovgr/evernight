package ru.evernight.ui.bean;

import lombok.Getter;
import lombok.Setter;
import ru.evernight.dao.statement.UserStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.User;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    @Getter
    @Setter
    private String login;
    @Getter
    @Setter
    private String password;
    @Getter
    private User user;

    @EJB
    private UserStatements us;

    public LoginBean() {
        user = new User();
        user.setName("Григорий");
    }

    public void tryLogin() throws EvernightException, IOException {
        user = us.login(login, password);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        session.setAttribute("username", user.getEmail());
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    public void logout() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) ec.getSession(false);
        session.invalidate();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }


    public String name() {
        return user.getSurname() + " " + user.getName();
    }
}
