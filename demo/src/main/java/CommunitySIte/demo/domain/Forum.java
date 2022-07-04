package CommunitySIte.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Entity
@Getter
@Setter
public class Forum {

    @Id
    @GeneratedValue
    @Column(name = "FORUM_ID")
    private Long id;
    private String forumName;
    private ForumType forumType;

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumManager> forumManagers = new ArrayList<>();

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id desc ")
    private List<Post> posts = new Stack<>();

    public Forum() {
        Category freeBoard = new Category("FreeBoard");
        freeBoard.setForum(this);
        categories.add(freeBoard);
    }

    public void addManager(Users user) {
        ForumManager forumManager = new ForumManager();
        forumManager.setForum(this);
        forumManager.setUser(user);
        user.getForum().add(forumManager);
        forumManagers.add(forumManager);
    }

    public void deleteManager(Users user) {
        forumManagers.remove(user);
    }

    public void addCategory(Category category) {
        category.setForum(this);
        categories.add(category);
    }
}
