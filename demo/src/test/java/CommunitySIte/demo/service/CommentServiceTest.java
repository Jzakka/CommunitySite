package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    ForumService forumService;

    @Test
    void writeComment() {
        Users user1 = new Users();
        user1.setUserName("testUser");
        Long userId = userService.join(user1);
        Forum forum = new Forum();
        forum.setForumName("testForum");
        Long forumId = forumService.openForum(forum);
        Long postId = postService.feedPost(userId, forumId, "testPost", "content");

        Long commentId = commentService.writeComment(userId, postId, "content");
        Comment comment = commentService.findComment(commentId);

        assertThat(comment.getContent()).isEqualTo("content");
    }

    @Test
    void findByWriter() {
    }

    @Test
    void updateComment() {
    }

    @Test
    void delete() {
    }
}