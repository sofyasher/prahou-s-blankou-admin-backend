package cz.prahousblankou.app.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @SequenceGenerator(name = "reservationGenerator", sequenceName = "RESERVATION_SEQUENCE", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservationGenerator")
    private Long id;

    @ManyToOne
    private Tour tour;

    @NonNull
    private Integer adults;

    @NonNull
    private Integer children;

    @NonNull
    private String clientEmail;

    @NonNull
    private String clientName;

    @NonNull
    private Long datetime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
