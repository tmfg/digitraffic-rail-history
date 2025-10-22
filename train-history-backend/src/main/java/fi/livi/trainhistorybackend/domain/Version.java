package fi.livi.trainhistorybackend.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public abstract class Version {
    private final long trainNumber;
    private final LocalDate departureDate;
    private final ZonedDateTime fetchDateTime;
    private final int versionId;

    protected Version(long trainNumber, LocalDate departureDate, ZonedDateTime fetchDateTime, int versionId) {
        this.trainNumber = trainNumber;
        this.departureDate = departureDate;
        this.fetchDateTime = fetchDateTime;
        this.versionId = versionId;
    }

    public long getTrainNumber() {
        return trainNumber;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public ZonedDateTime getFetchDateTime() {
        return fetchDateTime;
    }

    public int getVersionId() {
        return versionId;
    }

    public abstract Object getDetails();
    public abstract Object getData();
}
