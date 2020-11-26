package fi.livi.trainhistorybackend.repositories;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.trainhistorybackend.entities.Train;
import fi.livi.trainhistorybackend.entities.TrainId;


@Repository
@Transactional
public interface TrainRepository extends CrudRepository<Train, TrainId> {
    @Query("select distinct t from Train t " +
            "where t.id.trainNumber = ?1 and t.id.departureDate = ?2 " +
            "order by t.id.fetchDate asc")
    List<Train> findByTrainNumberAndDepartureDate(Long trainNumber, LocalDate departureDate);

    @Query("select distinct t.id.fetchDate from Train t" +
            " where t.version = (select max(version) from Train t1)")
    ZonedDateTime findLatestFetchDate();
}
