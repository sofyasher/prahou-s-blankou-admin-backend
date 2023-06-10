package cz.prahousblankou.app.web;

import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.exceptions.BadId;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ToursService;
import cz.prahousblankou.app.web.dto.TourModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/admin/tours")
public class ToursAdminController {
    @Autowired
    ToursService toursService;

    /**
     * Creates new tour entity with provided data.
     * Used on FE.
     * Test: testCreateTour
     */
    @PostMapping()
    public ResponseEntity<Tour> createTour(@RequestBody TourModel tour) {
        Tour createdTour = toursService.createTour(tour);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTour);
    }

    /**
     * Update a tour entity by provided data.
     * Used on FE.
     * Test: testCreateUndUpdateTour
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTour(@PathVariable Long id, @RequestBody TourModel tour) {
        Tour updatedTour;
        try {
            updatedTour = toursService.updateTour(id, tour);
        } catch (EntityNotFound | BadId e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().body(updatedTour);
    }

    /**
     * Returns tours for admin filtered by provided language.
     * Used ob FE.
     * Tests:
     * testGetToursForAdminInCzechAndEnglish,
     * testGetToursForAdminInUnknownLanguage
     */
    @GetMapping()
    public ResponseEntity<List<Tour>> getFilteredTours(
            @RequestParam("lang") Language lang) {
        List<Tour> tours = toursService.getFilteredTours(lang);
        return ResponseEntity.ok().body(tours);
    }

    /**
     * Sets attribute deleted=true to the tour with provided id in the database.
     * used on FE.
     * Test: testDeleteTour
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> revokeTour(@PathVariable("id") Long id) {
        try {
            toursService.revokeTour(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Sets attribute deleted=false to the tour with provided id in the database.
     * used on FE.
     * Test: testRenewTour
     */
    @PostMapping("/{id}/renew")
    public ResponseEntity<Object> renewTour(@PathVariable("id") Long id) {
        try {
            toursService.renewTour(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Can be used for importing tours from old-version json files.
     * Replaces all data in TOUR table by data from the files.
     * Test: testImportTours
     */
    @PostMapping("/import")
    public ResponseEntity<Object> importToursFromFiles() {
        try {
            toursService.importToursFromFiles();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
