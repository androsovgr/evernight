package ru.evernight.dao.statement;

import org.apache.commons.lang3.StringUtils;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

@Stateless
public class UserStatements extends CrudStatements<User> {

    public UserStatements() {
        super(User.class);
    }

    @Override
    public long create(User t) throws EvernightException {
        try {
            t.setPasswordHash(passwordHash(t.getEmail(), t.getPasswordHash()));
            em.persist(t);
            return t.getId();
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении в БД", e);

        }
    }

    @Override
    public void update(User user) throws EvernightException {
        try {
            User current = em.find(User.class, user.getId());
            if (!user.getEmail().equals(current.getEmail()) && StringUtils.isEmpty(user.getPasswordHash())) {
                throw new EvernightException("Для смены логина необходимо повторно ввести пароль");
            }
            if (StringUtils.isEmpty(user.getPasswordHash())) {
                user.setPasswordHash(current.getPasswordHash());
                if (user.equals(current)) {
                    throw new EvernightException("Пользователь не был изменен");
                }
            } else {
                user.setPasswordHash(passwordHash(user.getEmail(), user.getPasswordHash()));
            }
            em.merge(user);
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении в БД", e);

        }
    }

    private String passwordHash(String login, String password) throws EvernightException {
        try {
            byte[] salt = login.getBytes(StandardCharsets.UTF_8);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(hash);
        } catch (Exception e) {
            throw new EvernightException("Не удалось зашифровать пароль", e);
        }
    }
}
