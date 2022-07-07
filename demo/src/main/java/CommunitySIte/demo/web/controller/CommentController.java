package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.domain.PostType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.exception.NotAuthorizedException;
import CommunitySIte.demo.service.CommentService;
import CommunitySIte.demo.service.PostService;
import CommunitySIte.demo.web.argumentresolver.Login;
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
        boolean accessible = accessible(user, comment);
        if (!accessible) {
            //return "redirect:/forum/{forumId}/post/{postId}";
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }

        if (comment.getPostType() == PostType.NORMAL) {
            commentService.delete(commentId);
            return "redirect:/forum/{forumId}";
        } else {
            model.addAttribute("password", "");
            return "posts/enter-password";
        }

    }

    private boolean accessible(Users user, Comment comment) {
        //로그인한 사용자가 유동글 지우려려하면 리다이렉트->일반 사용자도 유동글 삭제 개입가능
        /*if(comment.getPostType()==PostType.ANONYMOUS && user !=null){
            log.info("accessible : 로그인 사용자가 댓글 수정접근");
            return false;
        }*/
        //유동이 일반 글 지우려하면 리다이렉트
        if(comment.getPostType()==PostType.NORMAL && user ==null ){
            log.info("accessible : 비로그인 사용자가 댓글 수정접근");
            return false;
        }
        //타임리프에서 이미 검증하지만 서버쪽에서도 이중 검증
        //일반 사용자가 자기글 아닌거 지우려 하면 리다이렉트
        else{
            if(!(comment.getUser()==null || comment.getUser().equals(user))){
                log.info("accessible : 작성자 불일치");
                //리다이렉트 말고 오류메시지나 오류창으로 넘어가게 만들기
                return false;
            }
        }
        return true;
    }

    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable Long commentId,
                         @Login Users user,
                         @ModelAttribute(name = "password") String password,
                         BindingResult bindingResult) {
        Comment comment = commentService.findComment(commentId);
        boolean accessible = accessible(user, comment);
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
            model.addAttribute("post", post);

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
