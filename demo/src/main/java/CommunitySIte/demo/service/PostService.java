package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.repository.ForumRepository;
import CommunitySIte.demo.repository.PostRepository;
import CommunitySIte.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static CommunitySIte.demo.domain.Post.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;

    public Long feedPost(Long userId, Long forumId, String title, Category category, String content) {
        Users user = userRepository.findOne(userId);
        Forum forum = forumRepository.findOne(forumId);
        Post post = createPost(user, forum, category, title, content);
        postRepository.save(post);
        return post.getId();
    }

    public Long feedPost(Long userId, Long forumId, String title, String content) {
        Users user = userRepository.findOne(userId);
        Forum forum = forumRepository.findOne(forumId);
        Post post = createPost(user, forum, title, content);
        postRepository.save(post);
        return post.getId();
    }

    public Long feedPost(Long forumId, String username, String title, String content) {
        Users user = createAnonymousUser(username);
        Forum forum = forumRepository.findOne(forumId);
        Post post = createPost(user, forum, title, content);
        postRepository.save(post);
        return post.getId();
    }

    private Users createAnonymousUser(String username) {
        Users anonymousUser = new Users();
        anonymousUser.setUserName(username);
        anonymousUser.setUserType(UserType.NONMEMBER);
        userRepository.save(anonymousUser);
        return anonymousUser;
    }

    @Transactional(readOnly = true)
    public Post showPost(Long id) {
        return postRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Post> showPostByTitle(String title){
        return postRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    public List<Post> showAllPost() {
        return postRepository.findAll();
    }

    public void like(Long id) {
        Post findPost = postRepository.findOne(id);
        findPost.increaseGood();
    }

    public void dislike(Long id) {
        Post findPost = postRepository.findOne(id);
        findPost.increaseBad();
    }

    public void increaseView(Long id) {
        Post one = postRepository.findOne(id);
        one.increaseViews();
    }

    public void update(Long id, String title, String content) {
        postRepository.update(id, title, content);
    }

    /**
     * 이거 테스트 돌리면 안 돌아감
     * 테스트에 @Transactional 붙이면 테스트용 트랜잭션만 돌아가고 서비스 로직은 적용 안됨
     * 테스트에 트랜잭션 빼고 패치타입 EAGER로 바꿔서 테스트하면 된다
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public List<Comment> showComments(Long id) {
        Post post = postRepository.findOne(id);
        System.out.println("class ==== " + post.getComments().getClass());
        return post.getComments();
    }

    @Transactional(readOnly = true)
    public List<Post> searchByPattern(String pattern) {
        return postRepository.findByPattern(pattern);
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }

}
