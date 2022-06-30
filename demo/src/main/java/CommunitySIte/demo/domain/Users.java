package CommunitySIte.demo.domain;

import CommunitySIte.demo.repository.dto.UserDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "USER")
@NoArgsConstructor
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
    private List<ForumManager> forum = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    public void update(UserDto userDto) {
        setUserName(userDto.getUserName());
        setPassword(userDto.getPassWord());
    }
}
