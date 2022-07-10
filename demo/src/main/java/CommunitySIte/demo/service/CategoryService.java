package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Category;
import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.Post;
import CommunitySIte.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public Long newCategory(Forum forum, String name) {
        Category category = new Category();
        category.setForum(forum);
        category.setCategoryName(name);
        return categoryRepository.create(category);
    }

    public List<Post> showPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findOne(categoryId);
        return category.getPosts();
    }

    public Category findOne(Long categoryId) {
        return categoryRepository.findOne(categoryId);
    }
}
