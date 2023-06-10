package cz.prahousblankou.app.service.impls;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.MeetingPoint;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import cz.prahousblankou.app.domain.repository.ImageRepository;
import cz.prahousblankou.app.domain.repository.TourRepository;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ToursService;
import cz.prahousblankou.app.service.converters.TourConvertTo;
import cz.prahousblankou.app.web.dto.TourModel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ToursServiceImpl implements ToursService {
    @Autowired
    TourRepository tourRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    TourConvertTo tourConvertTo;

    @Override
    public List<Tour> getFilteredTours(Language lang) {
        return tourRepository.getAllByLang(lang);
    }

    @Override
    public List<Tour> getFilteredTours(Language lang, TourType type) {
        return tourRepository.getAllByLangAndTypeAndDeleted(lang, type, false);
    }

    @Override
    public Tour getTourById(Long id) throws EntityNotFound {
        Optional<Tour> tour = tourRepository.findById(id);
        if (tour.isEmpty()) {
            throw new EntityNotFound("Tour with provided ID doesn't exist.");
        }

        return tour.get();
    }

    @Override
    public Tour createTour(TourModel tour) {
        Tour convertedTour = tourConvertTo.convertToEntity(tour);
        return tourRepository.save(convertedTour);
    }

    @Override
    @Transactional
    public Tour updateTour(Long id, TourModel tour) throws BadId, EntityNotFound {
        if (!id.equals(tour.getId())) {
            throw new BadId("Provided ID doesn't respond to entity ID.");
        }

        // check for existence in database
        Tour tourFromDB = getTourById(id);

        Tour convertedTour = tourConvertTo.convertToEntity(tour);
        return tourRepository.save(convertedTour);
    }

    @Override
    @Transactional
    public void revokeTour(Long id) throws EntityNotFound {
        Tour tour = getTourById(id);
        tourRepository.revokeTour(tour.getId());
    }

    @Override
    @Transactional
    public void renewTour(Long id) throws EntityNotFound {
        Tour tour = getTourById(id);
        tourRepository.renewTour(tour.getId());
    }

    @Override
    public void importToursFromFiles() throws IOException, ParseException {
        final String TOURS_PRAGUE_EN_PATH = "/json/TOURS_PRAGUE_EN.json";
        final String TOURS_PRAGUE_CZ_PATH = "/json/TOURS_PRAGUE_CZ.json";

        parseJsonAndSaveToDb(TOURS_PRAGUE_CZ_PATH, Language.CZ);
        parseJsonAndSaveToDb(TOURS_PRAGUE_EN_PATH, Language.EN);
    }

    private void parseJsonAndSaveToDb(String path, Language lang) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        InputStream is = getClass().getResourceAsStream(path);
        InputStreamReader reader = new InputStreamReader(is);
        Object obj = jsonParser.parse(reader);

        JSONArray toursJson = (JSONArray) obj;

        toursJson.forEach(tourJson -> {
            JSONObject tourJsonObj = (JSONObject) tourJson;
            Tour tour = new Tour();
            tour.setName((String) tourJsonObj.get("name"));
            tour.setLegend((String) tourJsonObj.get("legend"));
            tour.setDescription((String) tourJsonObj.get("description"));
            tour.setRestrictions((String) tourJsonObj.get("restrictions"));
            tour.setDuration(((Long) tourJsonObj.get("duration")).intValue());
            tour.setPrice((String) tourJsonObj.get("price"));
            tour.setType(TourType.valueOf((String) tourJsonObj.get("type")));
            tour.setMeetingPoint(MeetingPoint.valueOf((String) tourJsonObj.get("meetingPoint")));
            tour.setLang(lang);

            Set<Image> images = new HashSet<>();

            String imageUrl = (String) tourJsonObj.get("images");
            Image image = imageRepository.getByUrl(imageUrl);
            if (image == null) {
                Image imageToCreate = new Image();
                imageToCreate.setUrl(imageUrl);
                imageRepository.save(imageToCreate);
                images.add(imageToCreate);
            } else {
                images.add(image);
            }
            tour.setImages(images);

            if (tourRepository.getTourByNameAndLang(tour.getName(), tour.getLang()).isEmpty()) {
                tourRepository.save(tour);
            }
        });
    }
}
