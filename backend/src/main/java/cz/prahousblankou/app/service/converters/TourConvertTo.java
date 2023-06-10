package cz.prahousblankou.app.service.converters;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ImagesService;
import cz.prahousblankou.app.web.dto.TourModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TourConvertTo {

    @Autowired
    ImagesService imagesService;

    public Tour convertToEntity(TourModel tour) {
        Set<Image> images = tour.getImageIds().stream()
                .map(this::tryToFindImage)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Tour convertedTour = new Tour();
        Long id = tour.getId();
        if (id != null) {
            convertedTour.setId(id);
        }
        convertedTour.setName(tour.getName());
        convertedTour.setLegend(tour.getLegend());
        convertedTour.setDescription(tour.getDescription());
        convertedTour.setRestrictions(tour.getRestrictions());
        convertedTour.setMeetingPoint(tour.getMeetingPoint());
        convertedTour.setType(tour.getType());
        convertedTour.setDuration(tour.getDuration());
        convertedTour.setPrice(tour.getPrice());
        convertedTour.setLang(tour.getLang());
        convertedTour.setImages(images);
        convertedTour.setDeleted(tour.isDeleted());

        return convertedTour;
    }

    private Image tryToFindImage(Long id) {
        try {
            return this.imagesService.getImageById(id);
        } catch (EntityNotFound e) {
            e.printStackTrace();
            return null;
        }
    }
}
