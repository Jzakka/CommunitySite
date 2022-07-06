package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    private String loginId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(getId(), users.getId()) && Objects.equals(getLoginId(), users.getLoginId()) && Objects.equals(getUserName(), users.getUserName()) && Objects.equals(getPassword(), users.getPassword()) && getUserType() == users.getUserType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLoginId(), getUserName(), getPassword(), getUserType());
    }
}
