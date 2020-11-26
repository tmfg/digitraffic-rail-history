package fi.livi.trainhistoryupdater.repositories;

import fi.livi.trainhistoryupdater.entities.Composition;
import fi.livi.trainhistoryupdater.entities.TrainId;
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
    @Query("select coalesce(max(composition.version),0) from Composition composition")
    long getMaxVersion();

    @Modifying
    @Query("delete from Composition t where t.id.departureDate < ?1")
    void deleteByIdDepartureDateBefore(LocalDate localDate);
}
