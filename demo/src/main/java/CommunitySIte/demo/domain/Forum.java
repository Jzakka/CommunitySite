package CommunitySIte.demo.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "forum", cascade = CascadeType.ALL)
    private List<ForumManager> forumManagers;

    public void addManager(Users user) {
        ForumManager forumManager = new ForumManager();
        forumManager.setForum(this);
        forumManager.setUser(user);
        forumManagers.add(forumManager);
    }
}
