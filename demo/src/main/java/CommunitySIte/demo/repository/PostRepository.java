package CommunitySIte.demo.repository;

import CommunitySIte.demo.domain.Category;
import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.file.UploadFile;
import CommunitySIte.demo.web.controller.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


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

    public void update(Long id, String title, UploadFile imageFile, String content) {
        Post findPost = findOne(id);
        findPost.update(title, imageFile,  content);}

    public void delete(Long id) {
        em.createQuery("delete from Post p where p.id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    public List<Post> findPostsByPage(Criteria criteria, Forum forum) {
        return em.createQuery("select p from Post p where p.forum = :forum order by p.id desc", Post.class)
                .setParameter("forum", forum)
                .setFirstResult(criteria.pageStart())
                .setMaxResults(criteria.getPerPageNum())
                .getResultList();
    }

    public List<Post> findPostsByPage(Criteria criteria, Forum forum, Category category) {
        return em.createQuery("select p from Post p where p.forum = :forum and p.category = :category order by p.id desc", Post.class)
                .setParameter("forum", forum)
                .setParameter("category",category)
                .setFirstResult(criteria.pageStart())
                .setMaxResults(criteria.getPerPageNum())
                .getResultList();
    }

    public Post findPostAndComments(Long postId) {
        return em.createQuery("select p from Post p" +
                        " left join fetch p.comments" +
                        " join fetch p.forum" +
                        " where p.id=:id", Post.class).setParameter("id", postId)
                .getSingleResult();
    }

    public Post findPostAndForum(Long postId) {
       return em.createQuery("select p from Post p" +
                        " join fetch p.forum" +
                        " where p.id=:id", Post.class).setParameter("id", postId)
                .getSingleResult();
    }
}
