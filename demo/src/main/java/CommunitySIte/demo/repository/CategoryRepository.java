package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Long create(Category category){
        em.persist(category);
        return category.getId();
    }

    public Category findOne(Long id){
        return em.find(Category.class, id);
    }


    public Category findWithForumAndPosts(Long categoryId) {
       return em.createQuery("select distinct c from Category c" +
                        " join fetch c.forum" +
                        " left join fetch c.posts" +
                        " where c.id=:id", Category.class).setParameter("id", categoryId)
                .getSingleResult();
    }

    public void delete(Long categoryId) {
        em.createQuery("delete from Category c" +
                " where c.id = :id").setParameter("id", categoryId)
                .executeUpdate();
    }
}
