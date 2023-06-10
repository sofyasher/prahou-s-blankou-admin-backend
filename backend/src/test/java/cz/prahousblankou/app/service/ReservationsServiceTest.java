package cz.prahousblankou.app.service;

import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.MeetingPoint;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import cz.prahousblankou.app.domain.repository.ReservationRepository;
import cz.prahousblankou.app.domain.repository.TourRepository;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.converters.ReservationConvertTo;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ReservationsServiceTest {

    @Autowired
    ReservationsService reservationsService;

    @MockBean
    ReservationRepository reservationRepository;

    @MockBean
    TourRepository tourRepository;

    @MockBean
    ReservationConvertTo reservationConvertTo;

    @Test
    void getAllReservationsTest() {
        when(reservationRepository.findAll()).thenReturn(reservations());
        List<Reservation> reservations = reservationsService.getAllReservations();
        assertEquals(reservations.size(), 3);
    }

    @Test
    void getReservationByIdTest() throws EntityNotFound {
        when(reservationRepository.findById(302L)).thenReturn(Optional.ofNullable(reservations().get(2)));
        Reservation reservation = reservationsService.getReservationById(302L);
        assertEquals(reservation, reservations().get(2));
    }

    @Test
    void getReservationByBadIdTest() {
        when(reservationRepository.findById(5555L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFound.class, () -> reservationsService.getReservationById(5555L));
    }


    @Test
    void createReservation() throws EntityNotFound {
        ReservationModel reservationModel = reservationModel();
        Reservation reservation = reservations().get(2);
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tourData()));
        when(reservationConvertTo.convertToEntity(reservationModel)).thenReturn(reservation);
        reservationsService.createReservation(reservationModel);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateReservation() throws EntityNotFound, BadId {
        ReservationModel reservationModel = reservationModel();
        reservationModel.setId(303L);
        Reservation reservation = reservations().get(2);
        when(tourRepository.findById(123L)).thenReturn(Optional.of(tourData()));
        when(reservationRepository.findById(303L)).thenReturn(Optional.of(reservations().get(2)));
        when(reservationConvertTo.convertToEntity(reservationModel)).thenReturn(reservation);
        reservationsService.updateReservation(303L, reservationModel);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateReservationBadId() {
        ReservationModel reservationModel = reservationModel();
        reservationModel.setId(303L);
        assertThrows(BadId.class, () -> reservationsService.updateReservation(3003L, reservationModel));
    }

    @Test
    void deleteReservation() throws EntityNotFound {
        when(reservationRepository.findById(302L)).thenReturn(Optional.ofNullable(reservations().get(2)));
        reservationsService.deleteReservation(302L);
        verify(reservationRepository).delete(reservations().get(2));
    }

    private static Tour tourData() {
        return new Tour(123L, "name", "legend", "desc", "restr", MeetingPoint.KN, TourType.CLASSIC, 4, "2000 CZK", Language.EN, null, false);
    }

    private static ReservationModel reservationModel() {
        return new ReservationModel(null, 123L, 2, 0, "daniel@seznam.cz", "Daniel", 1642459294L);
    }

    private static List<Reservation> reservations() {
        return List.of(new Reservation[]{
                new Reservation(301L, tourData(), 2, 2, "ivan@mail.com", "Ivan", 1642458294L),
                new Reservation(302L, tourData(), 1, 2, "vladimir@mail.com", "Vladimir", 1642458294L),
                new Reservation(303L, tourData(), 2, 0, "daniel@seznam.cz", "Daniel", 1642459294L),
        });
    }
}
