package cz.prahousblankou.app.domain.repository;

import cz.prahousblankou.app.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image getByUrl(@Param("url") String url);

    List<Image> getAllByDeleted(@Param("deleted") boolean deleted);

    @Modifying
    @Query(value = "UPDATE Image SET deleted=true WHERE id=:id")
    void revokeImage(@Param("id") Long id);

    @Modifying
    @Query(value = "UPDATE image SET deleted=false WHERE id=:id", nativeQuery = true)
    void renewImage(@Param("id") Long id);
}
