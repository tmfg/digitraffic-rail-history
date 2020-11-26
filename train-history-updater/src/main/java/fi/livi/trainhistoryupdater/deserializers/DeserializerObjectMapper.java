package fi.livi.trainhistoryupdater.deserializers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fi.livi.trainhistoryupdater.entities.Train;

@Component
public class DeserializerObjectMapper extends ObjectMapper {
    @Autowired
    private TrainDeserializer trainDeserializer;

    @PostConstruct
    public void init() {
        final SimpleModule module = new SimpleModule();

        module.addDeserializer(Train.class, trainDeserializer);

        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        configure(SerializationFeature.INDENT_OUTPUT, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, true);

        registerModule(module);
        registerModule(new Jdk8Module());
        registerModule(new JavaTimeModule());
    }
}