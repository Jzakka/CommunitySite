package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/post")
public class PostController {

    private final PostService postService;
    private final ForumService forumService;

    @ModelAttribute("forumId")
    public Long forumId(@PathVariable Long forumId) {
        return forumId;
    }

    @PostMapping("/new")
    public String feed(@ModelAttribute("postForm") @Validated PostFeedForm postForm,
                       BindingResult bindingResult,
                       @PathVariable Long forumId,
                       Model model) {
        log.info("postForm = {}", postForm);
        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);

            //잘못 기입 될 때마다 쿼리로 포럼정보를 갱신해야하는 매우 비효율적인 구조.
            //------개선 필요---------
            Forum forum = forumService.showForum(forumId);
            List<Post> posts = forumService.showPosts(forumId);
            model.addAttribute("postForm",postForm);
            model.addAttribute("forum", forum);
            model.addAttribute("posts", posts);
            model.addAttribute("categories", forumService.showCategories(forumId));

            return "forums/forum";
        }

        postService.feedPost(forumId, postForm.getUsername(), postForm.getCategoryId(),
                postForm.getTitle(), postForm.getContent());
        return "redirect:/forum/{forumId}";
    }

    @PostMapping("/{postId}/update")
    public String update(@PathVariable Long postId,
                         @ModelAttribute("postForm") @Validated PostUpdateForm postForm,
                         BindingResult bindingResult){
        postService.update(postId, postForm.title, postForm.content);

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "posts/updateForm";
        }

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId) {
        postService.delete(postId);
        return "redirect:/forum/{forumId}";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId, Model model) {
        Post post = postService.showPost(postId);
        post.increaseViews();
        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentController.CommentForm());

        return "posts/post";
    }

    @GetMapping("/{postId}/update")
    public String updatePostForm(@PathVariable Long postId, Model model) {
        Post post = postService.findPost(postId);
        model.addAttribute("postId", postId);
        model.addAttribute("postForm", new PostFeedForm(post));

        return "posts/updateForm";
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostFeedForm {
        @NotBlank(message = "제목을 입력하세요.")
        private String title;
        @NotBlank(message = "이름 입력은 필수입니다.")
        private String username;
        @NotBlank(message = "내용을 입력하세요.")
        private String content;
        private Long categoryId;
        private Long forumId;

        public PostFeedForm(Post post) {
            title = post.getTitle();
            content = post.getContent();
        }
    }

    //에러메시지 파일이 yml로 설정이 안된다는듯...
    //일단은 생노가다로 defalut 메시지 설정

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUpdateForm {

        @NotNull(message = "임의 변경 불가")
        private Long id;
        @NotBlank(message = "제목을 입력하세요.")
        private String title;

        private String username;
        @NotBlank(message = "내용을 입력하세요")
        private String content;

        private Long categoryId;
        private Long forumId;
    }
}
