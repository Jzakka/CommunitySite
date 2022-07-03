package CommunitySIte.demo.controller;

import CommunitySIte.demo.domain.Category;
import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/new")
    public String feed(@PathVariable Long forumId, PostForm postForm) {
        log.info("postForm = {}", postForm);
        postService.feedPost(forumId, postForm.getUsername(), postForm.getCategoryId(),
                postForm.getTitle(), postForm.getContent());
        return "redirect:/forum/" + forumId;
    }

    @PostMapping("/{postId}/update")
    public String update(@PathVariable Long postId, @PathVariable Long forumId, PostForm postForm){
        postService.update(postId, postForm.title, postForm.content);

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId, @PathVariable Long forumId) {
        postService.delete(postId);

        return "redirect:/forum/{forumId}";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId,@PathVariable Long forumId, Model model) {
        Post post = postService.showPost(postId);
        PostForm postForm = new PostForm(post);
        model.addAttribute("forumId", forumId);
        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentController.CommentForm());

        return "posts/post";
    }

    @GetMapping("/{postId}/update")
    public String updatePostForm(@PathVariable Long postId, Model model) {
        Post post = postService.showPost(postId);
        model.addAttribute("postId", postId);
        model.addAttribute("postForm", new PostForm(post));

        return "posts/updateForm";
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostForm {
        private String title;
        private String username;
        private String content;
        private Long categoryId;
        private Long forumId;

        public PostForm(Post post) {
            title = post.getTitle();
            content = post.getContent();
        }
    }
}
