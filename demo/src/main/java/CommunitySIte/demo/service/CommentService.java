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
    public Long writeComment(Users user, Long postId, String content) {
        Post post = postRepository.findOne(postId);
        Comment comment = createComment(user, post, content);
        return commentRepository.save(comment);
    }

    @Transactional
    public Long writeComment(String username, String password, Long postId, String content) {
        Post post = postRepository.findOne(postId);
        Comment comment = createComment(username, password, post, content);
        return commentRepository.save(comment);
    }

    public Comment findComment(Long id) {
        return commentRepository.findOne(id);
    }

    public List<Comment> findByWriter(String name) {
        Users user = userRepository.findByName(name).get(0);
        List<Comment> comments = commentRepository.findByUser(user);
        return comments;
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.delete(id);
    }
}
