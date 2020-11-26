package fi.livi.trainhistoryupdater.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.trainhistoryupdater.entities.Train;
import fi.livi.trainhistoryupdater.entities.TrainId;


@Repository
@Transactional
public interface TrainRepository extends CrudRepository<Train, TrainId> {
    @Query("select coalesce(max(train.version),0) from Train train")
    long getMaxVersion();

    @Modifying
    @Query(nativeQuery = true, value = "delete from train where departure_date < ?1 limit 2500")
    void deleteByIdDepartureDateBefore(LocalDate localDate);
}
