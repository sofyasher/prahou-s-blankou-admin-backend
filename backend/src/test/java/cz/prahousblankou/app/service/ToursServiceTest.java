package cz.prahousblankou.app.service;

import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.MeetingPoint;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import cz.prahousblankou.app.domain.repository.TourRepository;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.converters.TourConvertTo;
import cz.prahousblankou.app.web.dto.TourModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ToursServiceTest {

    @Autowired
    ToursService toursService;

    @MockBean
    TourRepository tourRepository;

    @MockBean
    TourConvertTo tourConvertTo;

    @Test
    void getTourByNotExistedIdTest() {
        when(tourRepository.findById(555L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFound.class, () -> toursService.getTourById(555L));
    }

    @Test
    void getTourByIdTest() throws EntityNotFound {
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tourData()));
        Tour tour = toursService.getTourById(123L);
        assertEquals(tour, tourData());
    }

    @Test
    void getToursFilteredByLanguageTest() {
        when(tourRepository.getAllByLang(Language.CZ)).thenReturn(toursArray());
        List<Tour> tours = toursService.getFilteredTours(Language.CZ);
        assertEquals(tours, toursArray());
    }

    @Test
    void getToursFilteredByLanguageAndTypeTest() {
        when(tourRepository.getAllByLangAndTypeAndDeleted(Language.CZ, TourType.CHILDREN, false)).thenReturn(toursArray());
        List<Tour> tours = toursService.getFilteredTours(Language.CZ, TourType.CHILDREN);
        assertEquals(tours, toursArray());
    }

    @Test
    void createTourTest() {
        TourModel tourModel = tourModelData();
        tourModel.setImageIds(imageIds());
        Tour tour = tourData();
        when(tourConvertTo.convertToEntity(tourModel)).thenReturn(tour);
        toursService.createTour(tourModel);
        verify(tourRepository).save(tour);
    }

    @Test
    void updateTourTest() throws EntityNotFound, BadId {
        TourModel tourModel = tourModelData();
        tourModel.setId(123L);
        tourModel.setImageIds(imageIds());
        Tour tour = tourData();
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tour));
        when(tourConvertTo.convertToEntity(tourModel)).thenReturn(tour);
        toursService.updateTour(123L, tourModel);
        verify(tourRepository).save(tour);
    }

    @Test
    void updateTourBadIdTest() {
        TourModel tourModel = tourModelData();
        tourModel.setId(123L);
        tourModel.setImageIds(imageIds());
        assertThrows(BadId.class, () -> toursService.updateTour(125L, tourModel));
    }

    @Test
    void revokeTourTest() throws EntityNotFound {
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tourData()));
        toursService.revokeTour(123L);
        verify(tourRepository).revokeTour(123L);
    }

    @Test
    void renewTourTest() throws EntityNotFound {
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tourData()));
        toursService.renewTour(123L);
        verify(tourRepository).renewTour(123L);
    }

    private static Set<Long> imageIds() {
        HashSet<Long> images = new HashSet<>();
        images.add(4L);
        images.add(6L);
        return images;
    }

    private static TourModel tourModelData() {
        return new TourModel(null, "name", "legend", "desc", "restr", MeetingPoint.KN, TourType.CLASSIC, 4, "2000 CZK", Language.EN, null, false);
    }

    private static Tour tourData() {
        return new Tour(123L, "name", "legend", "desc", "restr", MeetingPoint.KN, TourType.CLASSIC, 4, "2000 CZK", Language.EN, null, false);
    }

    private static List<Tour> toursArray() {
        return List.of(new Tour[]{
                new Tour(123L, "name", "legend", "desc", "restr", MeetingPoint.KN, TourType.CLASSIC, 4, "2000 CZK", Language.EN, null, false),
                new Tour(124L, "name2", "legend2", "desc2", "restr2", MeetingPoint.MS, TourType.UNDISCOVERED, 4, "2000 CZK", Language.EN, null, false),
                new Tour(125L, "name3", "legend2", "desc2", "restr3", MeetingPoint.VN, TourType.CHILDREN, 5, "2500 CZK", Language.EN, null, false),
                new Tour(126L, "name4", "legend2", "desc2", "restr4", MeetingPoint.NY, TourType.THEMATIC, 7, "3000 CZK", Language.EN, null, false),
                new Tour(127L, "name5", "legend2", "desc2", "restr5", MeetingPoint.MS, TourType.UNDISCOVERED, 4, "2500 CZK", Language.CZ, null, false),
        });
    }
}
