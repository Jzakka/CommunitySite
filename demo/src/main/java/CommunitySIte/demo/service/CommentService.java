package CommunitySIte.demo.service;

import CommunitySIte.demo.domain.Comment;
import CommunitySIte.demo.repository.CommentRepository;
import CommunitySIte.demo.repository.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Long writeComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void updateComment(Long id, CommentDto dto) {
        commentRepository.update(id, dto);
    }

    public void delete(Long id) {
        commentRepository.delete(id);
    }
}
