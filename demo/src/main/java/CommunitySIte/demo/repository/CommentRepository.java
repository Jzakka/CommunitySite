package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public Long save(Comment comment) {
        em.persist(comment);
        Long id = comment.getId();
        return id;
    }

    public Comment findOne(Long id) {
        Comment findComment = em.find(Comment.class, id);
        return findComment;
    }

    public List<Comment> findAll() {
        List<Comment> resultList = em.createQuery("select c from Comment c", Comment.class).getResultList();
        return resultList;
    }

    public List<Comment> findByUser(Users user) {
        List<Comment> resultList = em.createQuery("select c from Comment c where c.user = :user", Comment.class)
                .setParameter("user", user).getResultList();
        return resultList;
    }

    public void update(Long id, String content) {
        Comment findComment = em.find(Comment.class, id);
        findComment.update(content);
    }

    public void delete(Long id) {
        em.createQuery("delete from Comment c where c.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
