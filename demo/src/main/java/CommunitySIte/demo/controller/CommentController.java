package CommunitySIte.demo.controller;

import CommunitySIte.demo.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.text.Style;

@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    //PostMapping 할 수 있는 방법 알아봐야할 듯(JS쓰던가, Html에서 폼으로 보내던가,...)
    @GetMapping("/{commentId}/delete")
    public String delete(@PathVariable Long forumId,
                         @PathVariable Long postId,
                         @PathVariable Long commentId){
        commentService.delete(commentId);

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @PostMapping("/new")
    public String comment(@PathVariable Long forumId, @PathVariable Long postId, CommentForm commentForm) {
        commentService.writeComment(commentForm.username, postId, commentForm.content);

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {
        private String username;
        private String content;
    }
}
