package ma.emsi.backendevents.repository;

import ma.emsi.backendevents.entity.Events;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.transaction.annotation.Transactional;

public interface EventsRepository extends JpaRepository<Events,Long> {
    List<Events> findAllByOrderByEventDateDesc();

    List<Events> findByEventNameContainingIgnoreCaseOrEventPlaceContainingIgnoreCaseOrderByEventDateDesc(String eventName, String eventPlace);

    @Transactional
    void deleteByEnventIdIn(List<Long> ids);
}
