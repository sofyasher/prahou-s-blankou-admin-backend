package cz.prahousblankou.app.web;

import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ReservationsService;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reservations")
public class ReservationsAdminController {
    @Autowired
    ReservationsService reservationsService;

    /**
     * Updates a reservation with provided id.
     * test: testUpdateReservation
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateReservation(@PathVariable Long id, @RequestBody ReservationModel reservation) {
        Reservation updatedReservation;
        try {
            updatedReservation = reservationsService.updateReservation(id, reservation);
        } catch (EntityNotFound | BadId e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().body(updatedReservation);
    }

    /**
     * Returns all reservations.
     * test: testGetAllReservations
     */
    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationsService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    /**
     * Returns a reservation by provided id.
     * test: testGetReservationById
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getReservationById(@PathVariable("id") Long id) {
        Reservation reservation;
        try {
            reservation = reservationsService.getReservationById(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }

        return ResponseEntity.ok().body(reservation);
    }

    /**
     * Removes a reservation with provided id from the database.
     * test: testDeleteReservation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable("id") Long id) {
        try {
            reservationsService.deleteReservation(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }

        return ResponseEntity.ok().build();
    }
}
