package CommunitySIte.demo.web;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.ForumType;
import CommunitySIte.demo.domain.UserType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.PostService;
import CommunitySIte.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@RequiredArgsConstructor
public class InitDataConstructor {
    private final UserService userService;
    private final ForumService forumService;

    private final PostService postService;

    @PostConstruct
    public void initUser() {
        Users user1 = new Users();
        user1.setUserName("Admin");
        user1.setLoginId("admin");
        user1.setPassword("admin");
        user1.setUserType(UserType.ADMIN);

        Users user2 = new Users();
        user2.setUserName("user1");
        user2.setLoginId("user1");
        user2.setPassword("1234");
        user2.setUserType(UserType.MEMBER);

        userService.join(user1);
        userService.join(user2);
    }

    @PostConstruct
    public void initForum(){
        Forum forum = new Forum();
        forum.setForumName("forum1");
        forum.setForumType(ForumType.MAIN);
        Forum forum2 = new Forum();
        forum2.setForumName("forum2");
        forum2.setForumType(ForumType.MAIN);
        Forum forum3 = new Forum();
        forum3.setForumType(ForumType.MINOR);
        forum3.setForumName("forum3");
        Forum forum4 = new Forum();
        forum4.setForumType(ForumType.MINOR);
        forum4.setForumName("forum4");
        Forum forum5 = new Forum();
        forum5.setForumType(ForumType.MAIN);
        forum5.setForumName("forum5");
        forumService.openForum(forum);
        forumService.openForum(forum2);
        forumService.openForum(forum3);
        forumService.openForum(forum4);
        forumService.openForum(forum5);

        Forum[] forums = new Forum[]{forum, forum2, forum3, forum4, forum4};

        for (Forum forum1 : forums) {
            for (int j = 0; j < 123; j++) {
                postService.feedPost(forum1.getId(),j+"", null,"user"+j,forum1.getCategories().get(0).getId(),"123",
                        "도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배도배");
            }
        }

    }
}
