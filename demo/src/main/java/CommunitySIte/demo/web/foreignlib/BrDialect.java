package CommunitySIte.demo.web.foreignlib;

import com.github.bufferings.thymeleaf.extras.nl2br.dialect.Nl2brDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BrDialect {
    @Bean
    public Nl2brDialect dialect() {
        return new Nl2brDialect();
    }
}
