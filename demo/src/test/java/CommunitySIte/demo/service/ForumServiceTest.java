package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.ForumType;
import CommunitySIte.demo.domain.UserType;
import CommunitySIte.demo.domain.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ForumServiceTest {

    @Autowired
    ForumService forumService;
    @Autowired
    UserService userService;

    @Test
    void openForum() {
        Forum forum = new Forum();
        forum.setForumName("TestForum");

        Long id = forumService.openForum(forum);

        Forum findForum = forumService.showForum(id);
        assertThat(findForum.getForumName()).isEqualTo(forum.getForumName());
    }

    @Test
    void showAllForum() {
        Forum forum = new Forum();
        forum.setForumName("TestForum");
        Forum forum2 = new Forum();
        forum2.setForumName("TestForum2");
        Forum forum3 = new Forum();
        forum3.setForumName("TestForum3");
        forumService.openForum(forum);
        forumService.openForum(forum2);
        forumService.openForum(forum3);

        List<Forum> forums = forumService.showAllForum();

        assertThat(forums.size()).isEqualTo(3);

    }

    @Test
    void setManager() {
        Users user = new Users();
        user.setUserName("testManager");
        user.setUserType(UserType.MEMBER);
        Long userId = userService.join(user);

        Forum forum = new Forum();
        forum.setForumName("testForum");
        forum.setForumType(ForumType.MINOR);
        Long forumId = forumService.openForum(forum);

        forumService.setManager(forumId, userId);

        Forum findForum = forumService.showForum(forumId);
        assertThat(findForum.getForumManagers().size()).isEqualTo(1);
    }

    @Test
    void addCategory() {
        Forum forum = new Forum();
        forum.setForumName("TestForum");
        Long id = forumService.openForum(forum);

        forumService.addCategory(id, "TestCategory1");
        forumService.addCategory(id, "TestCategory2");
        forumService.addCategory(id, "TestCategory3");

        Forum findForum = forumService.showForum(id);
        assertThat(findForum.getCategories().size()).isEqualTo(4);
    }

//    @Test
//    void dismissalManager(){
//
//    }
}