package com.recipetory.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticSearchConfig {
    @Value("#{'${spring.data.elasticsearch.hosts}'.split(',')}")
    private List<String> hosts;

    @Value("${spring.data.elasticsearch.port}")
    private int port;

    @Bean
    public ElasticsearchClient restClient() {
        List<HttpHost> hostList = new ArrayList<>();
        for (String host : hosts) {
            hostList.add(new HttpHost(host, port, "http"));
        }

        RestClient client = RestClient.builder(
                hostList.toArray(new HttpHost[hostList.size()]))
                .build();
        ElasticsearchTransport transport = new RestClientTransport(client, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }
}
