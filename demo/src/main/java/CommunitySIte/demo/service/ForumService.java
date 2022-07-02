package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.repository.ForumRepository;
import CommunitySIte.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;

    public Long openForum(Forum forum) {
        Long saveId = forumRepository.save(forum);
        return saveId;
    }

    @Transactional(readOnly = true)
    public Forum showForum(Long id) {
        return forumRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<Forum> showAllForum() {
        return forumRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Post> showPosts(Long id){
        Forum forum = forumRepository.findOne(id);
        return forum.getPosts();
    }

    public void setManager(Long forumId, Long userId) {

        Forum findForum = forumRepository.findOne(forumId);
        Users findUser = userRepository.findOne(userId);
        findForum.addManager(findUser);
    }

    public void addCategory(Long forumId, String categoryName) {
        Forum forum = forumRepository.findOne(forumId);
        forum.addCategory(new Category(categoryName));
    }

   /* public void dismissalManager(Long forumId, Long userId) {

    }*/
}
