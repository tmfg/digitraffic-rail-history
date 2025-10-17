package fi.livi.trainhistorybackend.pebbleextension;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Filter;

import java.util.HashMap;
import java.util.Map;

public class DateStringExtension extends AbstractExtension {

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<>();
        filters.put("dateString", new DateStringFilter());
        return filters;
    }
}
