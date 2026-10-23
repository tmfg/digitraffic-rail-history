package fi.livi.trainhistorybackend;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fi.livi.trainhistorybackend.domain.TrainVersion;
import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.entities.TrainId;
import fi.livi.trainhistorybackend.pebbleextension.DateStringExtension;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.loader.ClasspathLoader;

class VersionSelectionTemplateTest {

    private static TrainVersion trainVersion(final long version, final ZonedDateTime fetchDate) {
        final TrainId id = new TrainId();
        id.trainNumber = 1L;
        id.departureDate = LocalDate.of(2026, 1, 15);
        id.fetchDate = fetchDate;

        final Train train = new Train();
        train.id = id;
        train.version = version;

        return new TrainVersion(train);
    }

    private static String render(final List<TrainVersion> versions) throws Exception {
        final ClasspathLoader loader = new ClasspathLoader();
        loader.setPrefix("templates");

        final PebbleEngine engine = new PebbleEngine.Builder()
                .loader(loader)
                .extension(new DateStringExtension())
                .build();

        final StringWriter writer = new StringWriter();
        engine.getTemplate("modules/version-selection.peb").evaluate(writer, Map.of(
                "versions", versions,
                "versionUrl", "/history/trains/version",
                "selectedVersion", versions.getFirst()));
        return writer.toString();
    }

    @Test
    void winterFetchTimeIsShownInHelsinkiTime() throws Exception {
        final String html = render(List.of(
                trainVersion(3, ZonedDateTime.of(2026, 1, 15, 8, 30, 0, 0, ZoneOffset.UTC))));

        assertTrue(html.contains("15.01.2026 10:30:00 (v3)"), html);
    }

    @Test
    void summerFetchTimeIsShownInHelsinkiTime() throws Exception {
        final String html = render(List.of(
                trainVersion(4, ZonedDateTime.of(2026, 7, 15, 8, 30, 0, 0, ZoneOffset.UTC))));

        assertTrue(html.contains("15.07.2026 11:30:00 (v4)"), html);
    }
}
