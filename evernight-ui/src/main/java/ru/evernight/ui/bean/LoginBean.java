package ru.evernight.ui.bean;

import lombok.Getter;
import ru.evernight.model.User;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class LoginBean implements Serializable {
    @Getter
    private User user;

    public LoginBean() {
        user = new User();
        user.setName("Григорий");
    }

    public String name() {
        return user.getSurname() + " " + user.getName();
    }
}
