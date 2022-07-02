package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public Long save(Post post) {
        em.persist(post);
        Long id = post.getId();
        return id;
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findByTitle(String title) {
        return em.createQuery("select p from Post p where p.title = :title", Post.class)
                .setParameter("title", title)
                .getResultList();
    }

    public List<Post> findByPattern(String pattern) {
        return em.createQuery(
                "select p " +
                        "from Post p " +
                        "where upper(p.title)  like upper(:pattern)" +
                        "or upper(p.content) like upper(:pattern) ", Post.class)
                .setParameter("pattern", "%"+pattern+"%")
                .getResultList();
    }

    public List<Post> findAll() {
        List resultList = em.createQuery("select p from Post p").getResultList();
        return resultList;
    }

    public void update(Long id, String title, String content) {
        Post findPost = findOne(id);
        findPost.update(title, content);}

    public void delete(Long id) {
        em.createQuery("delete from Post p where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
