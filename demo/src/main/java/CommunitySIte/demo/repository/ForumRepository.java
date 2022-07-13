package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Forum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ForumRepository {

    private final EntityManager em;

    public Long save(Forum forum) {
        em.persist(forum);
        Long id = forum.getId();
        return id;
    }

    public Forum findOne(Long id) {
        Forum findForum = em.find(Forum.class, id);
        return findForum;
    }

    public List<Forum> findAll() {
        List resultList = em.createQuery("select f from Forum f").getResultList();
        return resultList;
    }

    public void delete(Long id) {
        em.createQuery("delete from Forum f where f.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public Integer getPostsCount(Forum forum) {
        return em.createQuery("select count (p) from Post p" +
                        " where p.forum=:forum",Long.class)
                .setParameter("forum", forum)
                .getSingleResult().intValue();
    }

    public Forum findForumAndCategory(Long forumId) {
        return em.createQuery("select distinct f from Forum f" +
                        " join fetch Category c" +
                        " on f.id=:id", Forum.class).setParameter("id", forumId)
                .getSingleResult();
    }

    public Forum findForumAndPosts(Long forumId) {
        return em.createQuery("select distinct f from Forum f" +
                        " join fetch f.posts" +
                        " where f.id=:id", Forum.class).setParameter("id", forumId)
                .getSingleResult();
    }

    public Forum findForumAndManager(Long forumId) {
        return em.createQuery("select distinct f from Forum f" +
                        " left join fetch f.forumManagers" +
                        " where f.id=:id", Forum.class).setParameter("id", forumId)
                .getSingleResult();
    }
}
