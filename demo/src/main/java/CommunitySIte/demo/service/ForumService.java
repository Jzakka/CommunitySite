package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.repository.ForumRepository;
import CommunitySIte.demo.repository.PostRepository;
import CommunitySIte.demo.repository.UserRepository;
import CommunitySIte.demo.web.controller.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

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

    @Transactional(readOnly = true)
    public List<Category> showCategories(Long id) {
        Forum forum = forumRepository.findOne(id);
        return forum.getCategories();
    }

    public void addCategory(Long forumId, String categoryName) {
        Forum forum = forumRepository.findOne(forumId);
        forum.addCategory(new Category(categoryName));
    }

    public List<Post> showPostsByPage(Criteria criteria, Forum forum) {
        return postRepository.findPostsByPage(criteria, forum);
    }

    public List<Post> showPostsByPage(Criteria criteria, Forum forum, Category category) {
        return postRepository.findPostsByPage(criteria, forum, category);
    }

    public Integer getPostsCount(Forum forum) {
        return forumRepository.getPostsCount(forum);
    }

    public Forum showForumWithCategory(Long forumId) {
        return forumRepository.findForumAndCategory(forumId);
    }

    public Forum showForumWithPosts(Long forumId) {
        return forumRepository.findForumAndPosts(forumId);
    }

    public Forum showForumWithManager(Long forumId) {
        return forumRepository.findForumAndManager(forumId);
    }

   /* public void dismissalManager(Long forumId, Long userId) {

    }*/
}
