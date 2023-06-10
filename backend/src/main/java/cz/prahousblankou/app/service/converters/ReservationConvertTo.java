package cz.prahousblankou.app.service.converters;

import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ToursService;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationConvertTo {

    @Autowired
    ToursService toursService;

    public Reservation convertToEntity(ReservationModel reservation) throws EntityNotFound {
        Tour tour = tryToFindTour(reservation.getTourId());

        Reservation convertedReservation = new Reservation();
        convertedReservation.setAdults(reservation.getAdults());
        convertedReservation.setChildren(reservation.getChildren());
        convertedReservation.setClientEmail(reservation.getClientEmail());
        convertedReservation.setClientName(reservation.getClientName());
        convertedReservation.setDatetime(reservation.getDatetime());
        convertedReservation.setTour(tour);

        return convertedReservation;
    }

    private Tour tryToFindTour(Long id) throws EntityNotFound {
        return this.toursService.getTourById(id);
    }
}
