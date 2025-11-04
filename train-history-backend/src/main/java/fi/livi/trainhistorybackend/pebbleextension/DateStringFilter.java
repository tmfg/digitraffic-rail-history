package fi.livi.trainhistorybackend.pebbleextension;

import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Pebble filter that parses ISO-8601 date strings and formats them.
 * Usage:
 *   {{ isoDateString | dateString("HH:mm:ss") }}
 *   {{ isoDateString | dateString("HH:mm:ss", "Europe/Helsinki") }}
 */
public class DateStringFilter implements Filter {

    @Override
    public Object apply(Object input, Map<String, Object> args, PebbleTemplate self,
                        EvaluationContext context, int lineNumber) {
        if (input == null) {
            return "";
        }

        String dateString = input.toString();
        if (dateString.isEmpty()) {
            return "";
        }

        try {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString);

            String pattern = "HH:mm:ss";
            if (args.containsKey("format")) {
                pattern = args.get("format").toString();
            }

            if (args.containsKey("timezone")) {
                String timezone = args.get("timezone").toString();
                ZoneId zoneId = ZoneId.of(timezone);
                zonedDateTime = zonedDateTime.withZoneSameInstant(zoneId);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return zonedDateTime.format(formatter);
        } catch (Exception e) {
            return dateString;
        }
    }

    @Override
    public List<String> getArgumentNames() {
        return List.of("format", "timezone");
    }
}
