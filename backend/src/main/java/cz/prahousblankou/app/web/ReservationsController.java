package cz.prahousblankou.app.web;

import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ReservationsService;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationsController {
    @Autowired
    ReservationsService reservationsService;

    /**
     * Creates new reservation with provided data.
     * test: testCreateReservation
     */
    @PostMapping()
    public ResponseEntity<Object> createReservation(@RequestBody ReservationModel reservation) {
        Reservation createdReservation;
        try {
            createdReservation = reservationsService.createReservation(reservation);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }
}
