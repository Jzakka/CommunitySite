package CommunitySIte.demo.controller;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;

    @GetMapping("/{id}")
    public String listForum(@PathVariable("id") Long forumId, Model model) {
        Forum forum = forumService.showForum(forumId);
        List<Post> posts = forumService.showPosts(forumId);
        model.addAttribute("postForm",new PostController.PostFeedForm());
        model.addAttribute("forum", forum);
        model.addAttribute("posts", posts);
        model.addAttribute("categories", forumService.showCategories(forumId));

        return "/forums/forum";
    }

    @PostConstruct
    public void init(){
        Forum forum = new Forum();
        forum.setForumName("forum1");
        Forum forum2 = new Forum();
        forum2.setForumName("forum2");
        Forum forum3 = new Forum();
        forum3.setForumName("forum3");
        Forum forum4 = new Forum();
        forum4.setForumName("forum4");
        Forum forum5 = new Forum();
        forum5.setForumName("forum5");
        forumService.openForum(forum);
        forumService.openForum(forum2);
        forumService.openForum(forum3);
        forumService.openForum(forum4);
        forumService.openForum(forum5);
    }
}
