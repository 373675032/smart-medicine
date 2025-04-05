package world.xuewei.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class ApplicationConfig {
    
    @PostConstruct
    public void init() {
        File uploadDir = new File("src/main/resources/static/upload");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
} 