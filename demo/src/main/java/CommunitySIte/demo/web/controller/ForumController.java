package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.service.ForumManageService;
import CommunitySIte.demo.service.ForumService;
import CommunitySIte.demo.service.UserService;
import CommunitySIte.demo.web.argumentresolver.Login;
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

import static CommunitySIte.demo.web.controller.CategoryController.isManager;

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

    @ModelAttribute("user")
    public Users user(@Login Users loginUser) {
        return loginUser;
    }

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
                            HttpServletRequest request,
                            @RequestParam(required = false) Integer page,
                            Model model,
                            Criteria criteria) {
        criteria.setPage(page == null ? 1 : page);
        log.info("criteria.page={}", criteria.getPage());
        Forum forum = forumService.showForumWithManager(forumId);
        Integer postsCount = forumService.getPostsCount(forum);

        PageCreator pageCreator = new PageCreator();
        pageCreator.setCriteria(criteria);
        pageCreator.setTotalCount(postsCount);

        model.addAttribute("postForm", new PostController.PostFeedForm());
        model.addAttribute("forum", forum);
        model.addAttribute("categories", forumService.showCategories(forumId));

        List<Post> list = forumService.showPostsByPage(criteria, forum);

        model.addAttribute("currentUrl", request.getRequestURL());
        model.addAttribute("posts", list);
        model.addAttribute("pageCreator", pageCreator);

        boolean isManager = isManager(loginUser, forum);
        model.addAttribute("isManager", isManager);

        return "/forums/forum";
    }

    @GetMapping("/{forumId}/manager")
    public String managerForm(@PathVariable Long forumId,
                              @ModelAttribute("managerForm") ManagerForm managerForm) {
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
        @NotBlank(message = "매니저를 설정할 유저ID를 입력해주세요")
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
