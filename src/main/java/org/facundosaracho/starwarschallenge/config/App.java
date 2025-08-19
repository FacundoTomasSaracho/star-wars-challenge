package org.facundosaracho.starwarschallenge.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.facundosaracho.starwarschallenge")
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
