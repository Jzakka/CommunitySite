package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    ForumService forumService;

    @Autowired
    CommentService commentService;

    Users user = new Users();
    Forum forum = new Forum();

    @Test
    void feedPost() {
        user.setUserName("testUser");
        userService.join(user);

        forum.setForumName("testForum");
        forumService.openForum(forum);
        Long id1 = postService.feedPost(user.getId(), forum.getId(), "title", "contents");
        Long id2 = postService.feedPost(user.getId(), forum.getId(), "title2", forum.getCategories().get(0), "contents");

        Post post1 = postService.showPost(id1);
        Post post2 = postService.showPost(id2);

        assertThat(post1.getTitle()).isEqualTo("title");
        assertThat(post2.getTitle()).isEqualTo("title2");
    }

    @Test
    void showPostByTitle() {
        user.setUserName("testUser");
        userService.join(user);

        forum.setForumName("testForum");
        forumService.openForum(forum);
        Long id = postService.feedPost(user.getId(), forum.getId(), "title", "contents");
        postService.feedPost(user.getId(), forum.getId(), "title", "contents");
        postService.feedPost(user.getId(), forum.getId(), "title", "contents");
        postService.feedPost(user.getId(), forum.getId(), "title", "contents");

        List<Post> posts = postService.showPostByTitle("title");
        assertThat(posts.size()).isEqualTo(4);
    }

    @Test
    void update() {
        user.setUserName("testUser");
        userService.join(user);

        forum.setForumName("testForum");
        forumService.openForum(forum);
        Long id = postService.feedPost(user.getId(), forum.getId(), "title", "contents");
        postService.update(id, "changedTitle", "changedContents");

        Post post = postService.showPost(id);
        assertThat(post.getTitle()).isEqualTo("changedTitle");
    }

    @Test
    @Rollback(value = false)
    void showComments() {
        user.setUserName("testUser");
        Long userId = userService.join(user);

        forum.setForumName("testForum");
        forumService.openForum(forum);
        Long id = postService.feedPost(user.getId(), forum.getId(), "title", "contents");

        commentService.writeComment(userId, id, "comment1");
        commentService.writeComment(userId, id, "comment2");
        commentService.writeComment(userId, id, "comment3");

        List<Comment> comments = postService.showComments(id);
        assertThat(comments.size()).isEqualTo(3);
    }

    @Test
    void searchByPattern() {
        user.setUserName("testUser");
        Long userId = userService.join(user);

        forum.setForumName("testForum");
        forumService.openForum(forum);
        Long id = postService.feedPost(user.getId(), forum.getId(), "title", "contents");

        List<Post> findPosts = postService.searchByPattern("cont");
        assertThat(findPosts.size()).isEqualTo(1);
    }

    @Test
    void delete() {
        user.setUserName("testUser");
        Long userId = userService.join(user);

        forum.setForumName("testForum");
        forumService.openForum(forum);
        Long id = postService.feedPost(user.getId(), forum.getId(), "title", "contents");
        Long id2 = postService.feedPost(user.getId(), forum.getId(), "title2", "contents");
        Long id3 = postService.feedPost(user.getId(), forum.getId(), "title3", "contents");

        postService.delete(id2);
        List<Post> posts = postService.showAllPost();
        assertThat(posts.size()).isEqualTo(2);
    }
}