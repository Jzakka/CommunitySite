package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Forum;
import CommunitySIte.demo.domain.ForumManager;
import CommunitySIte.demo.domain.Users;
import CommunitySIte.demo.repository.ForumRepository;
import CommunitySIte.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional
@RequiredArgsConstructor
public class ForumManageService {
    private final EntityManager em;

    public void updateUserToManager(Users user, Forum forum) {

        ForumManager forumManager = new ForumManager();
        forumManager.setUser(user);
        forumManager.setForum(forum);

        em.persist(forumManager);
    }
}
