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


}
