package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.PostType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.exception.NotAuthorizedException;
import CommunitySIte.demo.service.CommentService;
import CommunitySIte.demo.service.PostService;
import CommunitySIte.demo.web.argumentresolver.Login;
import CommunitySIte.demo.web.controller.access.AccessibilityChecker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

import static CommunitySIte.demo.web.controller.access.AccessibilityChecker.isManager;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    @ModelAttribute("forumId")
    public Long forumId(@PathVariable Long forumId) {
        return forumId;
    }
    @ModelAttribute("postId")
    public Long postId(@PathVariable Long postId) {
        return postId;
    }
    @ModelAttribute("postType")
    public PostType[] postType() {
        return PostType.values();
    }

    @ModelAttribute("user")
    public Users user(@Login Users loginUser) {
        return loginUser;
    }

    //PostMapping 할 수 있는 방법 알아봐야할 듯(JS쓰던가, Html에서 폼으로 보내던가,...)
    @GetMapping("/{commentId}/delete")
    public String enterPasswordOrNot(@Login Users user,
                                     @PathVariable Long commentId, Model model){
        Comment comment = commentService.findComment(commentId);
        boolean accessible = AccessibilityChecker.accessible(user, comment);
        if (!accessible) {
            //return "redirect:/forum/{forumId}/post/{postId}";
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }

        if (comment.getPostType() == PostType.NORMAL) {
            commentService.delete(commentId);
            return "redirect:/forum/{forumId}/post/{postId}";
        } else {
            model.addAttribute("password", "");
            return "posts/enter-password";
        }

    }


    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable Long commentId,
                         @Login Users user,
                         @ModelAttribute(name = "password") String password,
                         BindingResult bindingResult) {
        Comment comment = commentService.findComment(commentId);
        boolean accessible = AccessibilityChecker.accessible(user, comment);
        if (!accessible) {
            //return "redirect:/forum/{forumId}/post/{postId}";
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }

        if(!StringUtils.hasText(password)||(comment.getPassword() != null && !comment.getPassword().equals(password))){
            log.info("post.getPassword()={}", comment.getPassword());
            log.info("password={}", password);
            bindingResult.reject("password", "비밀번호를 올바르게 입력해주세요");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "posts/enter-password";
        }

        commentService.delete(commentId);

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @PostMapping("/new")
    public String comment(@PathVariable Long forumId, @PathVariable Long postId,@Login Users loginUser,
                          @ModelAttribute @Validated CommentForm commentForm, BindingResult bindingResult,
                          Model model) {
        if (loginUser == null) {
            if(!StringUtils.hasText(commentForm.getUsername())){
                bindingResult.rejectValue("username", "username","비회원 댓글은 이름기입이 필수입니다.");
            }
            if(!StringUtils.hasText(commentForm.getPassword())){
                bindingResult.rejectValue("password", "password","비회원 댓글은 비밀번호기입이 필수입니다.");
            }
        }

        if(bindingResult.hasErrors()){
            log.info("bindingResult = {}", bindingResult);
            Post post = postService.findPost(postId);
            model.addAttribute("forumId", forumId);
            model.addAttribute("forumName", post.getForum().getForumName());
            model.addAttribute("post", post);
            model.addAttribute("commentForm",commentForm);

            //게시글 수정 삭제 버튼이 사용자 타입, 게시글타입에 따라 보여야할지 말아야 할지 결정
            model.addAttribute("isPostAnonymous", post.getPostType() == PostType.ANONYMOUS);
            model.addAttribute("isUserWriter", (loginUser != null && post.getPostType() == PostType.NORMAL &&post.getUser().equals(loginUser)));
            model.addAttribute("isUserManager", (isManager(loginUser, post.getForum())));

            return "posts/post";
        }

        if ((loginUser == null)) {
            commentService.writeComment(commentForm.username,commentForm.password, postId, commentForm.content);
        } else {
            commentService.writeComment(loginUser, postId, commentForm.content);
        }

        return "redirect:/forum/{forumId}/post/{postId}";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentForm {

        private String username;
        private String password;
        @NotBlank(message = "내용을 입력하세요.")
        private String content;
    }
}
