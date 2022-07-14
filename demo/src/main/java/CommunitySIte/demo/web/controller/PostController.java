package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.domain.file.FileStore;
import CommunitySIte.demo.exception.NotAuthorizedException;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.PostService;
import CommunitySIte.demo.web.argumentresolver.Login;
import CommunitySIte.demo.web.controller.access.AccessibilityChecker;
import CommunitySIte.demo.web.controller.page.Criteria;
import CommunitySIte.demo.web.controller.page.PageCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Conventions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static CommunitySIte.demo.web.controller.ForumController.modelSetForumInfo;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/post")
public class PostController {

    private final PostService postService;
    private final ForumService forumService;
    private final FileStore fileStore;

    @ModelAttribute("forumId")
    public Long forumId(@PathVariable Long forumId) {
        return forumId;
    }

    @ModelAttribute("postType")
    public PostType[] postType() {
        return PostType.values();
    }

    @ModelAttribute("user")
    public Users user(@Login Users loginUser) {
        return loginUser;
    }

    @PostMapping("/new")
    public String feed(@ModelAttribute("postFeedForm") @Validated PostFeedForm postFeedForm,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes,
                       @Login  Users loginUser) throws IOException {
        //일반 사용자냐 유동이냐에 따라 투고 작성자 이름이 달라야함
        //일반 사용자
        //유동 사용자
        if (loginUser == null) {
            if(!StringUtils.hasText(postFeedForm.getUsername())){
                bindingResult.rejectValue("username", "username","비회원 투고는 이름기입이 필수입니다.");
            }
            if(!StringUtils.hasText(postFeedForm.getPassword())){
                bindingResult.rejectValue("password", "password","비회원 투고는 비밀번호기입이 필수입니다.");
            }
        }

        log.info("postFeedForm = {}", postFeedForm);
        if (bindingResult.hasErrors()) {
            log.info("PostController.bindingResult = {}", bindingResult.hashCode());

            //잘못 기입 될 때마다 쿼리로 포럼정보를 갱신해야하는 매우 비효율적인 구조.
            //리다이렉트 해보려고 했는데 무슨짓을 해도 BindingResult가 안넘어감
            //addFlashAttribute로 해봣는데 안넘어감
            //걍 절대 안됨 안되는건 안됨
            /**
             *찾아봤는데 컨트롤러는 호출 될 때마다 bindingResult를 덮어씌우는 듯
             * 그래서 ForumController로 가도 bindingResult의 결과는 다시 덮어씌워지나봄'
             * 실제 구글 검색해봐도 같은 컨트롤러 내에서의 예시만 있고 다른 컨트롤러로 가는 건 없다
             * 결국 해시맵에 에러 다시 담아서 리다이렉트...
             * 썩쎆쓰
             */
            //그냥 무식하게 모델에 때려박고 뷰화면 보여주는걸로
            //------개선 필요---------
            /*Forum forum = forumService.showForumWithManager(forumId);
            Integer postsCount = forumService.getPostsCount(forum);
            List<Category> categories = forumService.showCategories(forumId);

            PageCreator pageCreator = PageCreator.newPageCreator(page, criteria, postsCount);

            modelSetForumInfo(model, postFeedForm, forum, categories);

            List<Post> list = forumService.showPostsByPage(criteria, forum);

            ForumController.paging(request, model, pageCreator, list);

            AccessibilityChecker.checkIsManager(loginUser, model, forum);
*/
            //직접 에러 쑤셔박기
            Map<String, String> errors = new HashMap<>();

            if (bindingResult.hasFieldErrors("title")) {
                errors.put("title", bindingResult.getFieldError("title").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("username")) {
                errors.put("username", bindingResult.getFieldError("username").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("password")) {
                errors.put("password", bindingResult.getFieldError("password").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("content")) {
                errors.put("content", bindingResult.getFieldError("content").getDefaultMessage());
            }

            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:/forum/{forumId}";
//            return "forums/forum";
        }
        if ((loginUser == null)) {
            postService.feedPost(postFeedForm.forumId, postFeedForm.title, fileStore.storeFile(postFeedForm.imageFile),postFeedForm.username,
                    postFeedForm.categoryId, postFeedForm.password, postFeedForm.content);
        } else {
            postService.feedPost(postFeedForm.forumId, postFeedForm.title, fileStore.storeFile(postFeedForm.imageFile) , loginUser, postFeedForm.categoryId, postFeedForm.content);
        }

        return "redirect:/forum/{forumId}";
    }


    @PostMapping("/{postId}/update")
    public String update(@PathVariable Long postId,
                         @Login Users user,
                         @ModelAttribute("postForm") @Validated PostUpdateForm postForm,
                         BindingResult bindingResult) throws IOException {
        Post post = postService.findPostAndForum(postId);
        boolean accessible = AccessibilityChecker.accessible(user, post);
        if (!accessible) {
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }
        boolean updatable = updatable(user, postForm, bindingResult, post);
        if (!updatable) {
            return "redirect:/forum/"+post.getForum().getId()+"/post/{postId}";
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult = {}", bindingResult);
            return "posts/updateForm";
        }

        postService.update(postId, postForm.title, fileStore.storeFile(postForm.imageFile), postForm.content);
        return "redirect:/forum/"+post.getForum().getId()+"/post/{postId}";
    }

    private boolean updatable(Users user, PostUpdateForm postForm, BindingResult bindingResult, Post post) {
        log.info("post {} 가 update 가능한지 판독중 ...", post.getId());
        log.info("postForm = {}" , postForm);
        //유동 글 비밀번호 검증
        if(post.getPostType()==PostType.ANONYMOUS){
            if(!StringUtils.hasText(postForm.password)){
                log.info("updatable : 비밀번호 미입력");
                bindingResult.rejectValue("password","password","비밀번호를 입력해주세요");
            }
            else if (!post.getPassword().equals(postForm.password)) {
                log.info("updatable : 비밀번호 불일치");
                bindingResult.rejectValue("password","password","비밀번호가 다릅니다!");
            }
        }

        //일반 글 사용자 검증
        //
        else{
            if(!(post.getUser()==null || post.getUser().equals(user))){
                log.info("updatable : 작성자 불일치");
                //리다이렉트 말고 오류메시지나 오류창으로 넘어가게 만들기
                return false;
            }
        }
        return true;
    }

    @GetMapping("/{postId}/delete")
    public String enterPasswordOrNot(@PathVariable Long postId,
                                     @Login Users user, Model model) {
        Post post = postService.findPostAndForum(postId);
        Long forumId = post.getForum().getId();
        boolean accessible = AccessibilityChecker.accessible(user, post);
        if (!accessible) {
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }

        if (post.getPostType() == PostType.NORMAL) {
            postService.delete(postId);
            return "redirect:/forum/"+forumId;
        } else {
            model.addAttribute("password", "");
            return "posts/enter-password";
        }
    }

    @PostMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId,
                         @Login Users user,
                         @ModelAttribute(name = "password") String password,
                         BindingResult bindingResult){
        Post post = postService.findPostAndForum(postId);
        Long forumId = post.getForum().getId();
        boolean accessible = AccessibilityChecker.accessible(user, post);
        if (!accessible) {
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }
        if(!StringUtils.hasText(password)||(post.getPassword() != null && !post.getPassword().equals(password))){
            log.info("post.getPassword()={}", post.getPassword());
            log.info("password={}", password);
            bindingResult.reject("password", "비밀번호를 올바르게 입력해주세요");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "posts/enter-password";
        }

        postService.delete(postId);
        return "redirect:/forum/"+forumId;
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable Long postId,
                       @Login Users user,
                       Model model) {
        Post post = postService.showPostWithComment(postId);

        log.info("loginUser={}", user);
        log.info("post.user={}", post.getUser());
        post.increaseViews();

        model.addAttribute("forumId", post.getForum().getId());
        model.addAttribute("forumName", post.getForum().getForumName());
        model.addAttribute("post", post);
        model.addAttribute("commentForm", new CommentController.CommentForm());

        return "posts/post";
    }

    @GetMapping("/{postId}/update")
    public String updatePostForm(@PathVariable Long postId,
                                 @Login Users user,
                                 Model model) {
        Post post = postService.findPost(postId);

        boolean accessible = AccessibilityChecker.accessible(user, post);
        if (!accessible) {
            throw new NotAuthorizedException("인증되지 않은 사용자 접근입니다.");
        }

        model.addAttribute("forumId", post.getForum().getId());
        model.addAttribute("postId", postId);
        model.addAttribute("postForm", new PostUpdateForm(post));

        return "posts/updateForm";
    }




    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostFeedForm {
        @NotBlank(message = "제목을 입력하세요.")
        private String title;
        private String username;
        private String password;
        @NotBlank(message = "내용을 입력하세요.")
        private String content;
        private MultipartFile imageFile;
        private Long categoryId;
        private Long forumId;
    }

    //에러메시지 파일이 yml로 설정이 안된다는듯...
    //일단은 defalut 메시지 설정

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostUpdateForm {

        @NotNull(message = "임의 변경 불가")
        private Long id;
        @NotBlank(message = "제목을 입력하세요.")
        private String title;
        private PostType postType;
        private String password;
        private MultipartFile imageFile;
        @NotBlank(message = "내용을 입력하세요")
        private String content;

        public PostUpdateForm(Post post) {
            setTitle(post.getTitle());
            setContent(post.getContent());
            setPostType(post.getPostType());
        }
    }
}
