package br.com.videoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"br.com.videoproject.model.entity"})
@EnableJpaRepositories(basePackages = {"br.com.videoproject.model.repository"})
@ComponentScan(basePackages = {"br.com.videoproject"})
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
