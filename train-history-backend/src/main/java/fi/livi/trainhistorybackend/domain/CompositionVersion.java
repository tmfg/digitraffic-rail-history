package fi.livi.trainhistorybackend.domain;

import fi.livi.trainhistorybackend.entities.Composition;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class CompositionVersion extends Version {
    private final Composition compositionEntity;

    public CompositionVersion(Composition entity) {
        super(
            entity.id.trainNumber,
            entity.id.departureDate,
            entity.id.fetchDate,
            entity.version.intValue()
        );
        this.compositionEntity = entity;
    }

    public Long getVersion() {
        return compositionEntity.version;
    }

    @Override
    public Object getDetails() {
        List<Detail> details = new ArrayList<>();
        if (compositionEntity != null && compositionEntity.json != null) {
            JsonNode json = compositionEntity.json;

            if (json.has("trainType")) {
                details.add(new Detail("Junatyyppi", json.get("trainType").asText()));
            }
        }
        return details;
    }

    @Override
    public Object getData() {
        return compositionEntity != null ? compositionEntity.json.get("journeySections") : null;
    }


    public Composition getEntity() {
        return compositionEntity;
    }
}
