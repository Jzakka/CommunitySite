package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.service.CategoryService;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.web.argumentresolver.Login;
import CommunitySIte.demo.web.controller.page.Criteria;
import CommunitySIte.demo.web.controller.page.PageCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;

    @ModelAttribute("postType")
    public PostType[] postType() {
        return PostType.values();
    }

    @ModelAttribute("user")
    public Users user(@Login Users loginUser) {
        return loginUser;
    }

    @GetMapping("/{id}")
    public String listForum(@PathVariable("id") Long forumId,
                            HttpServletRequest request,
                            @RequestParam(required = false) Integer page,
                            Model model,
                            Criteria criteria) {
        criteria.setPage(page==null?1:page);
        log.info("criteria.page={}",criteria.getPage());
        Forum forum = forumService.showForum(forumId);
        List<Post> posts = forumService.showPosts(forumId);

        PageCreator pageCreator = new PageCreator();
        pageCreator.setCriteria(criteria);
        pageCreator.setTotalCount(posts.size());

        model.addAttribute("postForm",new PostController.PostFeedForm());
        model.addAttribute("forum", forum);
        model.addAttribute("categories", forumService.showCategories(forumId));

        List<Post> list = forumService.showPostsByPage(criteria,forum);

        model.addAttribute("currentUrl", request.getRequestURL());
        model.addAttribute("posts", list);
        model.addAttribute("pageCreator", pageCreator);

        return "/forums/forum";
    }
}
