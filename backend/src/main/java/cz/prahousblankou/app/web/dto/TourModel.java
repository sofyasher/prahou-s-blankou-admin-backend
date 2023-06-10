package cz.prahousblankou.app.web.dto;

import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.MeetingPoint;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TourModel {
    @Nullable
    private Long id;

    private String name;
    private String legend;
    private String description;
    private String restrictions;
    private MeetingPoint meetingPoint;
    private TourType type;
    private Integer duration;
    private String price;
    private Language lang;
    private Set<Long> imageIds;
    private boolean deleted;
}
