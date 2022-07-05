package CommunitySIte.demo.web.controller;

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
}
