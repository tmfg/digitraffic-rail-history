package fi.livi.trainhistorybackend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class TrainId implements Serializable {
    public Long trainNumber;
    public LocalDate departureDate;
    public ZonedDateTime fetchDate;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final TrainId trainId)) return false;

        if (!departureDate.equals(trainId.departureDate)) return false;
        if (!trainNumber.equals(trainId.trainNumber)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = trainNumber.hashCode();
        result = 31 * result + departureDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s: %s",departureDate,trainNumber);
    }
}
