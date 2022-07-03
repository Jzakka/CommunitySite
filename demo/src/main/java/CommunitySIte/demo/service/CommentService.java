package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.UserType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.CommentRepository;
import CommunitySIte.demo.repository.PostRepository;
import CommunitySIte.demo.repository.UserRepository;import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static CommunitySIte.demo.domain.Comment.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long writeComment(Long userId, Long postId, String content) {
        Users user = userRepository.findOne(userId);
        Post post = postRepository.findOne(postId);
        Comment comment = createComment(user, post, content);
        Long id = commentRepository.save(comment);
        return id;
    }

    @Transactional
    public Long writeComment(String username, Long postId, String content) {
        Users anonymousUser = createAnonymousUser(username);
        Post post = postRepository.findOne(postId);
        Comment comment = createComment(anonymousUser, post, content);
        Long id = commentRepository.save(comment);
        return id;
    }

    private Users createAnonymousUser(String username) {
        Users anonymousUser = new Users();
        anonymousUser.setUserType(UserType.NONMEMBER);
        anonymousUser.setUserName(username);
        userRepository.save(anonymousUser);
        return anonymousUser;
    }

    public Comment findComment(Long id) {
        return commentRepository.findOne(id);
    }

    public List<Comment> listAllComment() {
        return commentRepository.findAll();
    }

    public List<Comment> findByWriter(String name) {
        Users user = userRepository.findByName(name).get(0);
        List<Comment> comments = commentRepository.findByUser(user);
        return comments;
    }

    @Transactional
    public void updateComment(Long id, String content) {
        commentRepository.update(id, content);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.delete(id);
    }
}
