package CommunitySIte.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "POST_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORUM_ID")
    private Forum forum;

    private String title;
    private String content;
    private int views;
    private LocalDateTime lastModifiedDate;
    private int good;
    private int bad;

    @OneToMany(mappedBy = "post",  cascade = ALL)
    private List<Comment> comments = new ArrayList<>();

    private String image_id;

    public void update(String title, String content) {
        setTitle(title);
        setContent(content);
        setLastModifiedDate(LocalDateTime.now());
    }

    public void increaseGood() {
        good++;
    }

    public void increaseBad() {
        bad++;
    }

    public void increaseViews() {
        views++;
    }

    public static Post createPost(Users user, Forum forum, Category category, String title, String content) {
        Post post = new Post();
        post.setUser(user);
        post.setForum(forum);
        post.setCategory(category);
        post.setTitle(title);
        post.setContent(content);
        post.setLastModifiedDate(LocalDateTime.now());
        return post;
    }

    public static Post createPost(Users user, Forum forum, String title, String content) {
        Post post = new Post();
        post.setUser(user);
        post.setForum(forum);
        post.setCategory(forum.getCategories().get(0));
        post.setTitle(title);
        post.setContent(content);
        post.setLastModifiedDate(LocalDateTime.now());
        return post;
    }
}
