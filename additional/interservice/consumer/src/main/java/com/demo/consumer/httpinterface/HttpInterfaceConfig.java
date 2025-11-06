package com.demo.consumer.httpinterface;

import com.demo.consumer.restclient.RestClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class HttpInterfaceConfig {

    // Consume bean from RestClientConfig file.
    //private final RestClientConfig config;

    /*@Bean
    public ProviderHttpInterface webClientHttpInterface(){

        WebClient webClient = WebClient
                .builder().baseUrl("http://localhost:8081").build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        ProviderHttpInterface service = factory.createClient(ProviderHttpInterface.class);
        return service;
    }*/

   /* @Bean
    @LoadBalanced
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }*/

    @Bean
    public ProviderHttpInterface restClientHttpInterface(RestClient.Builder restClientBuilder) {

        RestClient restClient = restClientBuilder.baseUrl("http://provider").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        ProviderHttpInterface service = factory.createClient(ProviderHttpInterface.class);
        return service;
    }

}
