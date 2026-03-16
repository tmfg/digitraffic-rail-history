package fi.livi.trainhistoryupdater.deserializers;

import org.springframework.stereotype.Component;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import fi.livi.trainhistoryupdater.entities.Train;
import fi.livi.trainhistoryupdater.entities.TrainId;

@Component
public class TrainDeserializer extends AJsonDeserializer<Train> {
    public TrainDeserializer() {
        super(Train.class);
    }

    @Override
    public Train deserialize(final JsonParser jp, final DeserializationContext ctxt) throws JacksonException {
        final JsonNode node = jp.readValueAsTree();

        final Train train = new Train();

        final TrainId id = new TrainId();
        id.trainNumber = getLong(node.get("trainNumber"));
        id.departureDate = getLocalDate(node.get("departureDate"));
        train.version = getLong(node.get("version"));
        train.id = id;

        return train;
    }
}