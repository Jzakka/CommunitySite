package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.service.ForumManageService;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.UserService;
import CommunitySIte.demo.web.argumentresolver.Login;
import CommunitySIte.demo.web.controller.access.AccessibilityChecker;
import CommunitySIte.demo.web.controller.page.Criteria;
import CommunitySIte.demo.web.controller.page.PageCreator;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

import static CommunitySIte.demo.web.controller.PostController.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum")
public class ForumController {

    private final ForumService forumService;
    private final UserService userService;
    private final ForumManageService forumManageService;

    @ModelAttribute("postType")
    public PostType[] postType() {
        return PostType.values();
    }

    @ModelAttribute("forumTypes")
    public ForumType[] forumTypes(){
        return ForumType.values();}

    @GetMapping("/new")
    public String forumForm(@ModelAttribute("forumForm") ForumForm form) {
        return "forums/forum-form";
    }

    @PostMapping("/new")
    public String newForum(@ModelAttribute @Validated ForumForm form) {
        Forum forum = new Forum();
        forum.setForumName(form.forumName);
        forum.setForumType(form.forumType);
        forumService.openForum(forum);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String listForum(@PathVariable("id") Long forumId,
                            @Login Users loginUser,
                            @ModelAttribute(name = "postFeedForm") PostFeedForm postFeedForm,
                            HttpServletRequest request,
                            @RequestParam(required = false) Integer page,
                            Model model,
                            Criteria criteria) {
        Forum forum = forumService.showForumWithManager(forumId);
        Integer postsCount = forumService.getPostsCount(forum);
        List<Category> categories = forumService.showCategories(forumId);

        PageCreator pageCreator = PageCreator.newPageCreator(page, criteria, postsCount);

        modelSetForumInfo(model, postFeedForm, forum, categories);

        List<Post> list = forumService.showPostsByPage(criteria, forum);

        paging(request, model, pageCreator, list);

        AccessibilityChecker.checkIsManager(loginUser, model, forum);

        return "forums/forum";
    }

    public static void paging(HttpServletRequest request, Model model, PageCreator pageCreator, List<Post> list) {
        model.addAttribute("currentUrl", request.getRequestURL());
        model.addAttribute("posts", list);
        model.addAttribute("pageCreator", pageCreator);
    }

    public static void modelSetForumInfo(Model model, PostFeedForm postForm, Forum forum, List<Category> categories) {
        model.addAttribute("postForm", postForm);
        model.addAttribute("forum", forum);
        model.addAttribute("categories", categories);
    }


    @GetMapping("/{forumId}/manager")
    public String managerForm(@ModelAttribute("managerForm") ManagerForm managerForm,
                              @PathVariable Long forumId,
                              Model model) {
        model.addAttribute("forumId", forumId);
        return "forums/manager-form";
    }

    @PostMapping("/{forumId}/manager")
    public String userToManager(@PathVariable Long forumId,
                                @ModelAttribute @Validated ManagerForm managerForm,
                                BindingResult bindingResult) {
        Optional<Users> user = userService.searchUser(managerForm.getUserId());

        if (StringUtils.hasText(managerForm.userId) && !user.isPresent()) {
            bindingResult.rejectValue("userId", "userId","UserNotFound");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult={}", bindingResult);
            return "forums/manager-form";
        }

        Forum forum = forumService.showForum(forumId);
        forumManageService.updateUserToManager(user.get(),forum);

        return "redirect:/";
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ManagerForm {
        @NotBlank(message = "매니저를 설정할 유저 ID를 입력해주세요")
        String userId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ForumForm {
        @NotBlank(message = "포럼 명을 입력해주세요.")
        String forumName;
        ForumType forumType;
    }
}
