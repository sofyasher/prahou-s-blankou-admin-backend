package cz.prahousblankou.app.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationModel {
    @Nullable
    private Long id;

    private Long tourId;
    private Integer adults;
    private Integer children;
    private String clientEmail;
    private String clientName;
    private Long datetime;
}
