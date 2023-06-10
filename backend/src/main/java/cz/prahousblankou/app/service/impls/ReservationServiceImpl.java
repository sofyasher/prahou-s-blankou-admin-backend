package cz.prahousblankou.app.service.impls;

import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.domain.repository.ReservationRepository;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ReservationsService;
import cz.prahousblankou.app.service.converters.ReservationConvertTo;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationsService {
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ReservationConvertTo reservationConvertTo;

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(Long id) throws EntityNotFound {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new EntityNotFound("Reservation with provided ID doesn't exist");
        }

        return reservation.get();
    }

    @Override
    public Reservation createReservation(ReservationModel reservation) throws EntityNotFound {
        Reservation convertedReservation = reservationConvertTo.convertToEntity(reservation);
        return reservationRepository.save(convertedReservation);
    }

    @Override
    @Transactional
    public Reservation updateReservation(Long id, ReservationModel reservation) throws BadId, EntityNotFound {
        if (!id.equals(reservation.getId())) {
            throw new BadId("Provided ID doesn't respond to entity ID.");
        }

        // check for existence in database
        Reservation reservationFromDB = getReservationById(id);

        Reservation convertedReservation = reservationConvertTo.convertToEntity(reservation);
        return reservationRepository.save(convertedReservation);
    }

    @Override
    public void deleteReservation(Long id) throws EntityNotFound {
        Reservation reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }
}
