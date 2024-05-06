package fi.livi.trainhistoryupdater.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.livi.trainhistoryupdater.deserializers.DeserializerObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static final String DIGITRAFFIC_USER = "internal-digitraffic-thu";

    @Bean
    public WebClient webClient(final DeserializerObjectMapper objectMapper) {
        // more memory for default web-client
        return WebClient.builder()
                // add digitraffic-user header
                .defaultHeader("Digitraffic-User", DIGITRAFFIC_USER)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs().maxInMemorySize(30 * 1024 * 1024))
                        .codecs(codecs -> codecs
                                .defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
                        .build())
                .build();
    }
}
