package CommunitySIte.demo.controller;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts/new/{forumId}")
    public String feed(@PathVariable Long forumId, PostForm postForm){
        postService.feedPost(forumId, postForm.username, postForm.getTitle(), postForm.getContent());
        return "redirect:/forums/" + forumId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PostForm {
        private String title;
        private String username;
        private String content;
    }
}
