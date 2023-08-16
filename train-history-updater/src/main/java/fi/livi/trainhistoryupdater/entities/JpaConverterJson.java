package fi.livi.trainhistoryupdater.entities;

import java.io.IOException;
import jakarta.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JpaConverterJson implements AttributeConverter<Object, String> {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Object meta) {
    try {
      return objectMapper.writeValueAsString(meta);
    } catch (JsonProcessingException ex) {
      return null;
      // or throw an error
    }
  }

  @Override
  public Object convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readTree(dbData);
    } catch (IOException ex) {
      // logger.error("Unexpected IOEx decoding json from database: " + dbData);
      return null;
    }
  }

}