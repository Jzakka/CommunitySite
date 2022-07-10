package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(Users user) {
        em.persist(user);
        return user.getId();
    }

    public Users findOne(Long id) {
        Users findUser = em.find(Users.class, id);
        return findUser;
    }

    public List<Users> findByName(String name) {
        return em.createQuery("select u from Users u where u.userName = :name", Users.class)
                .setParameter("name", name).getResultList();
    }

    public Optional<Users> findByLoginId(String loginId) {
        return findAll().stream().filter(user -> user.getLoginId().equals(loginId)).findFirst();
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class).getResultList();
    }

    public void updateUser(Long id, String name, String password) {
        Users findUser = findOne(id);
        findUser.update(name, password);
    }

    public void deleteUser(Long id) {
        em.createQuery("delete from Users u where u.id = :id").
                setParameter("id", id)
                .executeUpdate();
    }
}
