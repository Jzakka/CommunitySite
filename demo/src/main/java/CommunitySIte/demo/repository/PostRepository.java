package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.repository.dto.PostDto;
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
        Post findPost = em.find(Post.class, id);
        return findPost;
    }

    public List<Post> findAll() {
        List resultList = em.createQuery("select p from Post p").getResultList();
        return resultList;
    }

    public void update(Long id, PostDto updateInfo) {
        Post findPost = findOne(id);
        findPost.update(updateInfo);}

    public void delete(Long id) {
        em.createQuery("delete from Post p where p.id = :id", Post.class)
                .setParameter("id", id)
                .executeUpdate();
    }
}
