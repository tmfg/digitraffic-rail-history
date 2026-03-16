package fi.livi.trainhistoryupdater.deserializers;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.cfg.JsonNodeFeature;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import fi.livi.trainhistoryupdater.entities.Train;

@Component
public class DeserializerObjectMapper {
    @Autowired
    private TrainDeserializer trainDeserializer;

    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        final SimpleModule module = new SimpleModule();

        module.addDeserializer(Train.class, trainDeserializer);

        this.objectMapper = JsonMapper.builder()
                .disable(SerializationFeature.INDENT_OUTPUT)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(JsonNodeFeature.WRITE_NULL_PROPERTIES)
                .enable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
                .addModule(module)
                .build();
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}