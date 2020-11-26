package fi.livi.trainhistoryupdater.entities;

import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class JsonEntity {
    public Long version;

    @EmbeddedId
    public TrainId id;

    @Convert(converter = JpaConverterJson.class)
    public JsonNode json;


    @Override
    public String toString() {
        return String.format("%s (%s)", id, version);
    }
}
