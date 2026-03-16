package fi.livi.trainhistorybackend.entities;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class JpaConverterJson implements AttributeConverter<Object, String> {

  private final static ObjectMapper objectMapper = new ObjectMapper();

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