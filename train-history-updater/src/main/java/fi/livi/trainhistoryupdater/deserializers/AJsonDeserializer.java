package fi.livi.trainhistoryupdater.deserializers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public abstract class AJsonDeserializer<A> extends StdDeserializer<A> {
    protected AJsonDeserializer(Class<A> vc) {
        super(vc);
    }

    Boolean getBoolean(JsonNode node) {
        if (node == null) {
            return null;
        } else {
            return node.asBoolean();
        }
    }

    String getString(JsonNode node) {
        if (node == null) {
            return null;
        } else {
            return node.asText();
        }
    }



    ZonedDateTime getZonedDateTime(final JsonNode node) {
        if (node == null) {
            return null;
        } else {
           return ZonedDateTime.parse(node.asText());
        }
    }

    LocalDateTime getLocalDateTime(final JsonNode node) {
        if (node == null) {
            return null;
        } else {
            final ZonedDateTime zonedDateTime = ZonedDateTime.parse(node.asText());
            return zonedDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        }
    }

    Integer getInteger(final JsonNode node) {
        if (node == null) {
            return null;
        } else {
            return node.asInt();
        }
    }
    LocalDate getLocalDate(final JsonNode node) {
        if (node == null) {
            return null;
        } else {
            return LocalDate.parse(node.asText());
        }
    }
    Long getLong(final JsonNode node) {
        if (node == null) {
            return null;
        } else {
            return node.asLong();
        }
    }

    <B> List<B> deserializeChildren(final DeserializationContext ctxt, final JsonNode children, final Class<B[]> childClass) throws JacksonException {
        return Arrays.asList(ctxt.readTreeAsValue(children, childClass));
    }
}
