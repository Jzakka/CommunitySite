package CommunitySIte.demo.domain;

import CommunitySIte.demo.repository.dto.CommentDto;
import lombok.AccessLevel;
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
    @Column(name = "COMMNET_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Users user;

    private String content;
    private LocalDateTime lastModifiedDate;

    public void update(CommentDto commentDto) {
        setContent(commentDto.getContent());
        setLastModifiedDate(LocalDateTime.now());
    }
}
