package fi.livi.trainhistoryupdater.deserializers;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import fi.livi.trainhistoryupdater.entities.Train;
import fi.livi.trainhistoryupdater.entities.TrainId;

@Component
public class TrainDeserializer extends AJsonDeserializer<Train> {
    @Override
    public Train deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        final Train train = new Train();

        TrainId id = new TrainId();
        id.trainNumber = getLong(node.get("trainNumber"));
        id.departureDate = getLocalDate(node.get("departureDate"));
        train.version = getLong(node.get("version"));
        train.id = id;

        return train;
    }
}