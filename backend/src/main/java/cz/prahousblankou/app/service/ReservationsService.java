package cz.prahousblankou.app.service;

import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.web.dto.ReservationModel;

import java.util.List;

public interface ReservationsService {
    List<Reservation> getAllReservations();

    Reservation getReservationById(Long id) throws EntityNotFound;

    Reservation createReservation(ReservationModel reservation) throws EntityNotFound;

    Reservation updateReservation(Long id, ReservationModel reservation) throws BadId, EntityNotFound;

    void deleteReservation(Long id) throws EntityNotFound;
}
