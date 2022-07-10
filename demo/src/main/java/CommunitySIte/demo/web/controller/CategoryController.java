package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.exception.NotAuthorizedException;
import CommunitySIte.demo.service.CategoryService;
import CommunitySIte.demo.service.ForumService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/category")
public class CategoryController {

    private final CategoryService categoryService;

    private final ForumService forumService;

    @ModelAttribute("postType")
    public PostType[] postType() {
        return PostType.values();
    }

    @ModelAttribute("user")
    public Users user(@Login Users loginUser) {
        return loginUser;
    }

    @GetMapping("/{categoryId}")
    public String showPostsByCategory(@PathVariable Long forumId,
                                      @PathVariable Long categoryId,
                                      @RequestParam(required = false) Integer page,
                                      @Login Users loginUser,
                                      HttpServletRequest request,
                                      Model model,
                                      Criteria criteria){
        criteria.setPage(page==null?1:page);
        Category category = categoryService.findOne(categoryId);
        Forum forum = forumService.showForum(forumId);
        List<Post> posts = categoryService.showPostsByCategory(categoryId);

        PageCreator pageCreator = new PageCreator();
        pageCreator.setCriteria(criteria);
        pageCreator.setTotalCount(posts.size());
        log.info("pageCreator={}", pageCreator);

        model.addAttribute("postForm",new PostController.PostFeedForm());
        model.addAttribute("forum", forum);
        model.addAttribute("categories", forumService.showCategories(forumId));

        List<Post> list = forumService.showPostsByPage(criteria, forum, category);

        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("posts", list);
        model.addAttribute("pageCreator", pageCreator);

        boolean isManager = isManager(loginUser, forum);
        model.addAttribute("isManager", isManager);

        return "forums/forum";
    }

    @GetMapping("/new")
    public String addCategoryForm(@PathVariable Long forumId,
                                  @Login Users loginUser,
                                  Model model) {
        Forum forum = forumService.showForum(forumId);

        boolean isManager = isManager(loginUser, forum);
        if (!isManager) {
            throw new NotAuthorizedException("매니저만 카테고리 추가가능");
        }

        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/addCategoryForm";
    }



    @PostMapping("/new")
    public String addCategory(@PathVariable Long forumId,
                              @Login Users loginUser,
                              @ModelAttribute @Validated CategoryForm categoryForm,
                              BindingResult bindingResult) {
        Forum forum = forumService.showForum(forumId);

        boolean isManager = isManager(loginUser, forum);
        if (!isManager) {
            throw new NotAuthorizedException("매니저만 카테고리 추가가능");
        }

        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);

            return "categories/addCategoryForm";
        }

        categoryService.newCategory(forum, categoryForm.categoryName);

        return "redirect:/forum/{forumId}";
    }


    public static boolean isManager(Users loginUser, Forum forum) {
        if(loginUser.getUserType()==UserType.ADMIN) return true;
        boolean isManager = false;
        List<ForumManager> forumManagers = forum.getForumManagers();
        for (ForumManager forumManager : forumManagers) {
            if (forumManager.getUser().equals(loginUser)) {
                isManager = true;
                break;
            }
        }
        return isManager;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CategoryForm {

        @NotBlank(message = "카테고리명을 입력하세요")
        private String categoryName;
    }
}
