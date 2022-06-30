package CommunitySIte.demo.domain;

import CommunitySIte.demo.repository.dto.PostDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "POST_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "post")
    private List<Comment> comments=new ArrayList<>();
    private String image_id;

    public void update(PostDto postDto) {
        setTitle(postDto.getTitle());
        setTitle(postDto.getContent());
        setGood(postDto.getGood());
        setBad(postDto.getBad());
        setLastModifiedDate(LocalDateTime.now());
    }

    public void increaseGood() {
        good++;
    }

    public void increaseBad() {
        bad++;
    }
}
