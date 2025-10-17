package fi.livi.trainhistorybackend.config;

import fi.livi.trainhistorybackend.pebbleextension.DateStringExtension;
import io.pebbletemplates.pebble.extension.Extension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PebbleConfiguration {

    @Bean
    public Extension dateStringExtension() {
        return new DateStringExtension();
    }
}
