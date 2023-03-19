package com.harsha.textsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "/")
@SpringBootApplication
public class TextsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextsearchApplication.class, args);
    }

}
