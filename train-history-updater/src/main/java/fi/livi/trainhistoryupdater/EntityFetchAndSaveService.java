package fi.livi.trainhistoryupdater;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import fi.livi.trainhistoryupdater.deserializers.DeserializerObjectMapper;
import fi.livi.trainhistoryupdater.entities.JsonEntity;
import fi.livi.trainhistoryupdater.entities.TrainId;

@Service
public class EntityFetchAndSaveService {
    @Autowired
    private DeserializerObjectMapper objectMapper;

    @Value("${digitraffic-url:https://rata.digitraffic.fi}")
    private String DIGITRAFFIC_URL;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().build());
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Transactional
    public <EntityType extends JsonEntity> Iterable<EntityType> pollForNewEntities(Supplier<Long> versionSupplier, String url, String name, Supplier<EntityType> entityFactory, CrudRepository<EntityType, TrainId> repository) throws IOException {
        final long maxVersion = versionSupplier.get();
        final String urlString;
        if (url.startsWith("http")) {
            urlString = String.format(url, maxVersion);
        } else {
            urlString = String.format(url, DIGITRAFFIC_URL, maxVersion);
        }

        byte[] responesBytes = getRestTemplate().getForObject(urlString, byte[].class);
        final JsonNode jsonNode = objectMapper.readTree(responesBytes);

        ZonedDateTime fetchDate = ZonedDateTime.now();
        List<EntityType> entities = new ArrayList<>();
        long newMaxVersion = Long.MIN_VALUE;
        for (final JsonNode node : jsonNode) {
            EntityType entityBase = entityFactory.get();
            final EntityType entity = setEntityFields(fetchDate, node, entityBase);

            entities.add(entity);

            if (entity.version > newMaxVersion) {
                newMaxVersion = entity.version;
            }
        }

        log.info("{} -> {}: {} new {}. {}", maxVersion, newMaxVersion, entities.size(), name, entities);

        Iterable<EntityType> savedEntities = repository.saveAll(entities);

        return savedEntities;
    }

    private <T extends JsonEntity> T setEntityFields(ZonedDateTime fetchDate, JsonNode node, T entity) {
        TrainId id = new TrainId();
        id.trainNumber = getLong(node.get("trainNumber"));
        id.departureDate = getLocalDate(node.get("departureDate"));
        id.fetchDate = fetchDate;
        entity.version = getLong(node.get("version"));
        entity.id = id;
        entity.json = node;
        return entity;
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
}
