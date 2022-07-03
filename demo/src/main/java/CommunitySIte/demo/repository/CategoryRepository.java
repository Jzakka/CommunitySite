package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public Category findOne(Long id){
        return em.find(Category.class, id);
    }
}
