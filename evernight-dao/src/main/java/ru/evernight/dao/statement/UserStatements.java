package ru.evernight.dao.statement;

import org.apache.commons.lang3.StringUtils;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.AccountStatus;
import ru.evernight.model.User;
import ru.evernight.model.User_;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

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

    public User login(String login, String password) throws EvernightException {
        String passwordHash = passwordHash(login, password);
        CriteriaBuilder cb = em
                .getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> ur = cq.from(User.class);
        cq.where(cb.and(cb.equal(cb.upper(ur.get(User_.email)), login.toUpperCase()), cb.equal(ur.get(User_.passwordHash), passwordHash)));
        TypedQuery<User> tq = em.createQuery(cq);
        List<User> founded = tq.getResultList();
        if (founded.size() == 0) {
            throw new EvernightException("Логин или пароль введен неправильно");
        }
        if (founded.get(0).getStatus() == AccountStatus.LOCKED) {
            throw new EvernightException("Аккаунт заблокирован");
        }
        return founded.get(0);
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
