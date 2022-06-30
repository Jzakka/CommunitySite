package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public Long save(Users user) {
        em.persist(user);
        Long id = user.getId();
        return id;
    }

    public Users findOne(Long id) {
        Users findUser = em.find(Users.class, id);
        return findUser;
    }

    public List<Users> findAll() {
        List<Users> resultList = em.createQuery("select u from Users u", Users.class).getResultList();
        return resultList;
    }

    public void updateUser(Long id, UserDto updateInfo) {
        Users findUser = findOne(id);
        findUser.update(updateInfo);
    }

    public void deleteUser(Long id) {
        em.createQuery("delete from Users u where u.id = :id",Users.class).
                setParameter("id", id)
                .executeUpdate();
    }
}
