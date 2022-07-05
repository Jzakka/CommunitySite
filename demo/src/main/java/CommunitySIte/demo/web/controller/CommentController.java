package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.service.CommentService;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    //PostMapping 할 수 있는 방법 알아봐야할 듯(JS쓰던가, Html에서 폼으로 보내던가,...)
    @GetMapping("/{commentId}/delete")
    public String delete(@PathVariable Long forumId,
                         @PathVariable Long postId,
                         @PathVariable Long commentId){
        commentService.delete(commentId);

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @PostMapping("/new")
    public String comment(@PathVariable Long forumId, @PathVariable Long postId,
                          @ModelAttribute @Validated CommentForm commentForm, BindingResult bindingResult,
                          Model model) {
        if(bindingResult.hasErrors()){
            log.info("bindingResult = {}", bindingResult);
            Post post = postService.findPost(postId);
            model.addAttribute("forumId", forumId);
            model.addAttribute("post", post);

            return "posts/post";
        }

        commentService.writeComment(commentForm.username, postId, commentForm.content);

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {
        @NotBlank(message = "이름 입력은 필수입니다.")
        private String username;
        @NotBlank(message = "내용을 입력하세요.")
        private String content;
    }
}
