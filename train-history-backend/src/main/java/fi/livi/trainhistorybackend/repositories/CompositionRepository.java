package fi.livi.trainhistorybackend.repositories;

import fi.livi.trainhistorybackend.entities.Composition;
import fi.livi.trainhistorybackend.entities.TrainId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Repository
@Transactional
public interface CompositionRepository extends CrudRepository<Composition, TrainId> {
    @Query("select distinct t from Composition t " +
            "where t.id.trainNumber = ?1 and t.id.departureDate = ?2 " +
            "order by t.id.fetchDate asc")
    List<Composition> findByTrainNumberAndDepartureDate(Long trainNumber, LocalDate departureDate);
}
