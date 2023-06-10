package cz.prahousblankou.app.domain.entity;

import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.MeetingPoint;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tour {
    @Id
    @SequenceGenerator(name = "tourGenerator", sequenceName = "TOUR_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tourGenerator")
    private Long id;

    @NonNull
    private String name;

    @NonNull
    @Column(length = 500)
    private String legend;

    @NonNull
    @Column(length = 500)
    private String description;

    private String restrictions;

    @Enumerated(EnumType.STRING)
    private MeetingPoint meetingPoint;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TourType type;

    @NonNull
    private Integer duration;

    @NonNull
    private String price;

    @NonNull
    @Enumerated(EnumType.STRING)
    private Language lang;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tour_image",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private Set<Image> images;

    private boolean deleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return id.equals(tour.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
