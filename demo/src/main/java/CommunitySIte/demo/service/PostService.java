package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.domain.file.UploadFile;
import CommunitySIte.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static CommunitySIte.demo.domain.Post.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final ForumRepository forumRepository;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    public void feedPost(Long forumId, String title, UploadFile imageFile, String anonymousUsername, Long categoryId,
                         String password, String content ) {
        Forum forum = forumRepository.findOne(forumId);
        Category category = categoryRepository.findOne(categoryId);
        Post post = Post.createPost(forum, title, imageFile, category, anonymousUsername, password, content);
        postRepository.save(post);
    }

    public void feedPost(Long forumId, String title,UploadFile imageFile, Users user, Long categoryId, String content) {
        Forum forum = forumRepository.findOne(forumId);
        Category category = categoryRepository.findOne(categoryId);
        Post post = Post.createPost(forum, title, imageFile, user, category, content);
        postRepository.save(post);
    }

    private Users createAnonymousUser(String username) {
        Users anonymousUser = new Users();
        anonymousUser.setUserName(username);
        anonymousUser.setUserType(UserType.NONMEMBER);
        userRepository.save(anonymousUser);
        return anonymousUser;
    }

    @Transactional(readOnly = true)
    public Post findPost(Long id) {
        return postRepository.findOne(id);
    }

    public Post showPost(Long id) {
        Post post = postRepository.findOne(id);
        post.increaseViews();
        return post;
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

    public void update(Long id, String title, UploadFile imageFile, String content) {
        postRepository.update(id, title,imageFile, content);
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
        return post.getComments();
    }

    @Transactional(readOnly = true)
    public List<Post> searchByPattern(String pattern) {
        return postRepository.findByPattern(pattern);
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }

    public Post showPostWithComment(Long postId) {
        return postRepository.findPostAndComments(postId);
    }

    public Post findPostAndForum(Long postId) {
        return postRepository.findPostAndForum(postId);
    }
}
