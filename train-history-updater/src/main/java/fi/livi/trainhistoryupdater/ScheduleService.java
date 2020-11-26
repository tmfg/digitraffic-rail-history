package fi.livi.trainhistoryupdater;

import java.io.IOException;
import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import fi.livi.trainhistoryupdater.entities.Composition;
import fi.livi.trainhistoryupdater.entities.Train;
import fi.livi.trainhistoryupdater.repositories.CompositionRepository;
import fi.livi.trainhistoryupdater.repositories.TrainRepository;

@Service
public class ScheduleService {
    public static final int NUMBER_OF_DAYS_TO_RETAIN = 14;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private CompositionRepository compositionRepository;

    @Autowired
    private EntityFetchAndSaveService entityFetchService;

    @Value("${digitraffic-url:https://rata.digitraffic.fi}")
    private String DIGITRAFFIC_URL;

    @PostConstruct
    private void setup() {
        log.info("Fetching from {}", DIGITRAFFIC_URL);
    }

    @Scheduled(fixedRate = 24 * (1000 * 60 * 60))
    public void deleteOldCompositions() {
        final LocalDate deleteLimit = LocalDate.now().minusDays(NUMBER_OF_DAYS_TO_RETAIN);

        compositionRepository.deleteByIdDepartureDateBefore(deleteLimit);
        log.info("Deleted compositions before {}", deleteLimit);
    }

    @Scheduled(fixedDelay = 15 * (1000 * 60))
    public void deleteOldTrains() {
        final LocalDate deleteLimit = LocalDate.now().minusDays(NUMBER_OF_DAYS_TO_RETAIN);
        log.info("Starting to delete trains before {}", deleteLimit);

        trainRepository.deleteByIdDepartureDateBefore(deleteLimit);
        log.info("Ended deleting trains before {}", deleteLimit);
    }

    @Scheduled(fixedDelay = 10 * 1000)
    public void getTrains() throws IOException {
        synchronized (Train.class) {
            entityFetchService.pollForNewEntities(trainRepository::getMaxVersion, "%s/api/v1/trains?version=%s", "trains", Train::new, trainRepository);
        }
    }

    @Scheduled(fixedDelay = 30 * 1000)
    public void getCompositions() throws IOException {
        synchronized (Composition.class) {
            entityFetchService.pollForNewEntities(compositionRepository::getMaxVersion, "%s/api/v1/compositions?version=%s", "compositions", Composition::new, compositionRepository);
        }
    }
}
