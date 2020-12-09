package io.github.leandroleitec.imageconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:src/main/resources/application.properties")
public class ImageConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageConverterApplication.class, args);
    }

}
