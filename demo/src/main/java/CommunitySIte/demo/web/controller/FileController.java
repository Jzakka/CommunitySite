package CommunitySIte.demo.web.controller;

import CommunitySIte.demo.domain.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileStore fileStore;

    @ResponseBody
    @GetMapping("/image/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        log.info("filename={}", filename);
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
