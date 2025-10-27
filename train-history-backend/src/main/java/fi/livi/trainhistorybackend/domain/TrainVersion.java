package fi.livi.trainhistorybackend.domain;

import fi.livi.trainhistorybackend.entities.Train;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;



public class TrainVersion extends Version {
    private final Train trainEntity;

    public TrainVersion(Train entity) {
        super(
            entity.id.trainNumber,
            entity.id.departureDate,
            entity.id.fetchDate,
            entity.version.intValue()
        );
        this.trainEntity = entity;
    }

    public Long getVersion() {
        return trainEntity.version;
    }

    @Override
    public Object getDetails() {
        List<Detail> details = new ArrayList<>();
        if (trainEntity != null && trainEntity.json != null) {
            JsonNode json = trainEntity.json;

            if (json.has("operatorShortCode")) {
                details.add(new Detail("Operaattori", json.get("operatorShortCode").asText()));
            }
            if (json.has("trainType")) {
                details.add(new Detail("Tyyppi", json.get("trainType").asText()));
            }
            if (json.has("timetableAcceptanceDate")) {
                String isoDate = json.get("timetableAcceptanceDate").asText();
                String formattedDate = formatDateTime(isoDate);
                details.add(new Detail("Hyväksytty", formattedDate));
            }
            if (json.has("runningCurrently")) {
                details.add(new Detail("Kulussa", json.get("runningCurrently").asText()));
            }
            if (json.has("cancelled")) {
                details.add(new Detail("Peruttu", json.get("cancelled").asText()));
            }
            if (json.has("commuterLineID")) {
                details.add(new Detail("Lähilinjatunnus", json.get("commuterLineID").asText()));
            }
        }
        return details;
    }

    private String formatDateTime(String isoDateTime) {
        try {
            ZonedDateTime dateTime = ZonedDateTime.parse(isoDateTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            return dateTime.format(formatter);
        } catch (Exception e) {
            return isoDateTime;
        }
    }

    @Override
    public Object getData() {
        return trainEntity != null ? trainEntity.json.get("timeTableRows") : null;
    }

    public Train getEntity() {
        return trainEntity;
    }
}
