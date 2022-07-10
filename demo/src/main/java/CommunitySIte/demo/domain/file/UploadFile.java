package CommunitySIte.demo.domain.file;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {

    private String uploadFileName;
    private String storeFileName;
}
