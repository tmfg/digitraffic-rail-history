package fi.livi.trainhistorybackend.service;

import fi.livi.trainhistorybackend.domain.TrainVersion;
import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.repositories.TrainRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TrainService {
    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public List<TrainVersion> findByNumberAndDate(final Long trainNumber, final LocalDate departureDate) {
        final List<Train> entities = trainRepository.findByTrainNumberAndDepartureDate(trainNumber, departureDate);

        return entities.stream()
            .map(TrainVersion::new)
            .collect(Collectors.toList());
    }


    public Optional<TrainVersion> findByVersion(Long trainNumber, LocalDate departureDate, Long version) {
        return trainRepository.findByTrainNumberAndDepartureDateAndVersion(trainNumber, departureDate, version)
            .map(TrainVersion::new);
    }
}
