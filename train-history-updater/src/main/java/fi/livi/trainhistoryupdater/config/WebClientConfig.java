package fi.livi.trainhistoryupdater.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static final String DIGITRAFFIC_USER = "internal-digitraffic-thu";

    @Bean
    public WebClient webClient() {
        // more memory for default web-client
        return WebClient.builder()
                // add digitraffic-user header
                .defaultHeader("Digitraffic-User", DIGITRAFFIC_USER)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs
                                .defaultCodecs().maxInMemorySize(50 * 1024 * 1024))
                        .build())
                .build();
    }
}
