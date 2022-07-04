package CommunitySIte.demo.controller;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.service.CategoryService;
import CommunitySIte.demo.service.ForumService;
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
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/forum/{forumId}/category")
public class CategoryController {

    private final CategoryService categoryService;

    private final ForumService forumService;

    @GetMapping("/{categoryId}")
    public String showPostsByCategory(@PathVariable Long forumId,
                                      @PathVariable Long categoryId,
                                      Model model){
        Forum forum = forumService.showForum(forumId);
        List<Post> posts = categoryService.showPostsByCategory(categoryId);
        model.addAttribute("postForm",new PostController.PostFeedForm());
        model.addAttribute("forum", forum);
        model.addAttribute("posts", posts);
        model.addAttribute("categories", forumService.showCategories(forumId));

        return "forums/forum";
    }

    @GetMapping("/new")
    public String addCategoryForm(@PathVariable Long forumId, Model model) {
        Forum forum = forumService.showForum(forumId);
        model.addAttribute("categoryForm", new CategoryForm());
        return "categories/addCategoryForm";
    }

    @PostMapping("/new")
    public String addCategory(@PathVariable Long forumId,
                              @ModelAttribute @Validated CategoryForm categoryForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("errors = {}", bindingResult);

            return "categories/addCategoryForm";
        }

        Forum forum = forumService.showForum(forumId);
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
