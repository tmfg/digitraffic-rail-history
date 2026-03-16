package fi.livi.trainhistoryupdater.entities;

import jakarta.persistence.AttributeConverter;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public class JpaConverterJson implements AttributeConverter<Object, String> {

  private final static ObjectMapper objectMapper = JsonMapper.builder().build();

  @Override
  public String convertToDatabaseColumn(Object meta) {
    try {
      return objectMapper.writeValueAsString(meta);
    } catch (JacksonException ex) {
      return null;
    }
  }

  @Override
  public Object convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.readTree(dbData);
    } catch (JacksonException ex) {
      return null;
    }
  }

}