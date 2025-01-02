package edu.qingchenjia.heimacomments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class HeiMaCommentsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeiMaCommentsApplication.class, args);
        log.info("""
          
            ------------------------------------------------------------
            \tHeiMaCommentsApplication started successfully!
            \tApi-Doc:\thttp://localhost:8080/doc.html
            ------------------------------------------------------------
            """);
    }

}
