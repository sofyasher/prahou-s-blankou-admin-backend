package cz.prahousblankou.app.service;

import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.web.dto.TourModel;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface ToursService {
    List<Tour> getFilteredTours(Language lang);

    List<Tour> getFilteredTours(Language lang, TourType type);

    Tour getTourById(Long id) throws EntityNotFound;

    Tour createTour(TourModel tour);

    Tour updateTour(Long id, TourModel tour) throws BadId, EntityNotFound;

    void revokeTour(Long id) throws EntityNotFound;

    void renewTour(Long id) throws EntityNotFound;

    void importToursFromFiles() throws IOException, ParseException;
}
