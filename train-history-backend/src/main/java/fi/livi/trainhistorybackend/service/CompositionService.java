package fi.livi.trainhistorybackend.service;

import fi.livi.trainhistorybackend.domain.CompositionVersion;
import fi.livi.trainhistorybackend.entities.Composition;
import fi.livi.trainhistorybackend.repositories.CompositionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompositionService {
    private final CompositionRepository compositionRepository;

    public CompositionService(CompositionRepository compositionRepository) {
        this.compositionRepository = compositionRepository;
    }

    public List<CompositionVersion> findByNumberAndDate(Long trainNumber, LocalDate departureDate) {
        List<Composition> entities = compositionRepository.findByTrainNumberAndDepartureDate(trainNumber, departureDate);

        return entities.stream()
            .map(CompositionVersion::new)
            .collect(Collectors.toList());
    }

    public Optional<CompositionVersion> findByVersion(Long trainNumber, LocalDate departureDate, Long version) {
        return compositionRepository.findByTrainNumberAndDepartureDateAndVersion(trainNumber, departureDate, version)
            .map(CompositionVersion::new);
    }
}
