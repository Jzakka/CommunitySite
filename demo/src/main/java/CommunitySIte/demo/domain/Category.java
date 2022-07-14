package CommunitySIte.demo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "CATEGORY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORUM_ID")
    private Forum forum;
    private String categoryName;

    @OneToMany(mappedBy = "category" ,orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("id desc")
    private List<Post> posts = new ArrayList<>();

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
