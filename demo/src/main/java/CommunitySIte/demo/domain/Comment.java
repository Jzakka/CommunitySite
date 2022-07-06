package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Users user;

    private String anonymousUserName;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    private String password;

    private String content;
    private LocalDateTime lastModifiedDate;

    public void update(String content) {
        setContent(content);
        setLastModifiedDate(LocalDateTime.now());
    }

    /**
     * 회원이 쓴 댓글 생성메서드
     * @param user
     * @param post
     * @param content
     * @return
     */
    public static Comment createComment(Users user, Post post, String content) {
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPostType(PostType.NORMAL);
        comment.setPost(post);
        comment.setContent(content);
        comment.setLastModifiedDate(LocalDateTime.now());
        return comment;
    }

    /**
     * 비회원이 쓴 댓글 생성메서드
     * @param username
     * @param password
     * @param post
     * @param content
     * @return
     */
    public static Comment createComment(String  username, String password, Post post, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setPostType(PostType.ANONYMOUS);
        comment.setAnonymousUserName(username);
        comment.setPassword(password);
        comment.setContent(content);
        comment.setLastModifiedDate(LocalDateTime.now());
        return comment;
    }
}
