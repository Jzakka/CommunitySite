package CommunitySIte.demo.domain;

import CommunitySIte.demo.domain.file.UploadFile;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post implements Accessible{

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

    @Enumerated(EnumType.STRING)
    private PostType postType;

    private String title;
    private String content;
    private String anonymousUserName;
    private String password;
    private int views;
    private LocalDateTime lastModifiedDate;
    private int good;
    private int bad;

    /**
     * cascade=REMOVE가 작동안함...
     * OnDelete는 되는데 왜인지 잘 모르겠음. 일단 이걸로 설정
     */
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("id desc")
    private List<Comment> comments = new ArrayList<>();

    @Embedded
    private UploadFile imageFile;

    public static Post createPost(Forum forum, String title,UploadFile imageFile, Category category, String anonymousUsername, String password, String content) {
        Post post = new Post();
        post.setForum(forum);
        post.setTitle(title);
        post.setImageFile(imageFile);
        post.setCategory(category);
        post.setPostType(PostType.ANONYMOUS);
        post.setAnonymousUserName(anonymousUsername);
        post.setPassword(password);
        post.setContent(content);
        post.setLastModifiedDate(LocalDateTime.now());
        return post;
    }

    public static Post createPost(Forum forum, String title,UploadFile imageFile, Users user, Category category, String content) {
        Post post = new Post();
        post.setForum(forum);
        post.setPostType(PostType.NORMAL);
        post.setTitle(title);
        post.setImageFile(imageFile);
        post.setUser(user);
        post.setCategory(category);
        post.setContent(content);
        post.setLastModifiedDate(LocalDateTime.now());
        return post;
    }

    public void update(String title, UploadFile imageFile, String content) {
        setTitle(title);
        setContent(content);
        setImageFile(imageFile);
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
}
