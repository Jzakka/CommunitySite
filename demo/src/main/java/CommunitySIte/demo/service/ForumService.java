package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.ForumManager;
import CommunitySIte.demo.domain.ForumType;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;

    public Long openForum(Forum forum) {
        Long saveId = forumRepository.save(forum);
        return saveId;
    }

    public Forum showForum(Long id) {
        return forumRepository.findOne(id);
    }

    public List<Forum> showAllForum() {
        return forumRepository.findAll();
    }

    public void setManager(Forum forum, Users user) {
        forum.addManager(user);
    }
}
