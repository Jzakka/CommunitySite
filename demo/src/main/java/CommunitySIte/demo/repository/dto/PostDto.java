package CommunitySIte.demo.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private String image_id;
    private int good;
    private int bad;
}
