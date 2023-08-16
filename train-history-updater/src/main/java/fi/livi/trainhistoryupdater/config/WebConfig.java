package fi.livi.trainhistoryupdater.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import fi.livi.trainhistoryupdater.deserializers.DeserializerObjectMapper;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private DeserializerObjectMapper httpOutputJsonObjectMapper;

    @Bean
    public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(httpOutputJsonObjectMapper);
        return converter;
    }

    @Override
    public void extendMessageConverters(final List<HttpMessageConverter<?>> converters) {
        converters.add(customJackson2HttpMessageConverter());
    }
}