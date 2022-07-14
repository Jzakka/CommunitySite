package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.*;
import CommunitySIte.demo.exception.NotAuthorizedException;
import CommunitySIte.demo.service.CategoryService;
import CommunitySIte.demo.service.ForumService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static CommunitySIte.demo.web.controller.ForumController.*;
import static CommunitySIte.demo.web.controller.access.AccessibilityChecker.*;
import static CommunitySIte.demo.web.controller.access.AccessibilityChecker.isManager;
import static CommunitySIte.demo.web.controller.page.PageCreator.*;

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
        Category category = categoryService.findWithForumAndPosts(categoryId);
        Integer postsCount = category.getPosts().size();
        List<Category> categories = forumService.showCategories(forumId);

        PageCreator pageCreator = newPageCreator(page, criteria, postsCount);

        modelSetForumInfo(model,category.getForum(),categories);

        List<Post> list = forumService.showPostsByPage(criteria, category.getForum(), category);

        paging(request,model,pageCreator,list);

        checkIsManager(loginUser,model,category.getForum());

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

    @GetMapping("/delete")
    public String delete(@PathVariable Long forumId,
                                  @RequestParam Long categoryId,
                                  @Login Users loginUser) {
        Forum forum = forumService.showForum(forumId);

        boolean isManager = isManager(loginUser, forum);
        if (!isManager) {
            throw new NotAuthorizedException("매니저만 카테고리 삭제가능");
        }

        categoryService.deleteCategory(categoryId);
        return "redirect:/forum/{forumId}";
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


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CategoryForm {

        @NotBlank(message = "카테고리명을 입력하세요")
        private String categoryName;
    }
}
