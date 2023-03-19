package com.harsha.textsearch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchClientConfiguration extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.rest.uris}")
    private String connectionUrl;

    @Override
//    @Bean
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(connectionUrl)
                .build();
    }
}
