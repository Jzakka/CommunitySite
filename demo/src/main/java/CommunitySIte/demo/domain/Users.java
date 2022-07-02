package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;
    private String userName;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserType userType = UserType.MEMBER;

    @OneToMany(mappedBy = "user")
    private List<ForumManager> forum = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Users(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public void update(String userName, String password) {
        setUserName(userName);
        setPassword(password);
    }
}
