package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;
    private String userName;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "user")
    private List<ForumManager> forum;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
}
