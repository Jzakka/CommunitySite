package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.repository.PostRepository;
import CommunitySIte.demo.repository.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long feedPost(Post post) {
        Long savedId = postRepository.save(post);
        return savedId;
    }

    public Post showPost(Long id) {
        return postRepository.findOne(id);
    }

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

    public void update(Long id, PostDto dto) {
        postRepository.update(id,dto );
    }

    public List<Comment> showComments(Long id) {
        Post findPost = postRepository.findOne(id);
        List<Comment> comments = findPost.getComments();
        return comments;
    }

    public void delete(Long id) {
        postRepository.delete(id);
    }
}
