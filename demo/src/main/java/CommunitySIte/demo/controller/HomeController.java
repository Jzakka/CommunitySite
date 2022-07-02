package CommunitySIte.demo.controller;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ForumService forumService;

    @GetMapping("/")
    public String home(Model model) {
        List<Forum> forums = forumService.showAllForum();
        model.addAttribute("forums", forums);
        return "/home";
    }
}
