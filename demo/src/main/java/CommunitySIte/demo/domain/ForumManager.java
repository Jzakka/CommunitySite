package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ForumManager {

    @Id
    @GeneratedValue
    @Column(name = "FORUM_MANAGER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORUM_ID")
    private Forum forum;

}
