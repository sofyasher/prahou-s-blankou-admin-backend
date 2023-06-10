package cz.prahousblankou.app.domain.repository;

import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> getAllByLang(@Param("lang") Language lang);

    List<Tour> getAllByLangAndTypeAndDeleted(@Param("lang") Language lang, @Param("type") TourType type, @Param("deleted") boolean deleted);

    Optional<Tour> getTourByNameAndLang(@Param("name") String name, @Param("lang") Language lang);

    @Modifying
    @Query(value = "UPDATE Tour SET deleted=true WHERE id=:id")
    void revokeTour(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE tour SET deleted=false WHERE id=:id", nativeQuery = true)
    void renewTour(@Param("id") Long id);
}
