package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
}
