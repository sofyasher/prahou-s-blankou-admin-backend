package cz.prahousblankou.app.web;

import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ToursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/tours")
public class ToursController {
    @Autowired
    ToursService toursService;

    /**
     * Returns tours for users filtered by provided language and tour type.
     * Used on FE.
     * Test: getToursByTypeAndLanguage
     */
    @GetMapping()
    public ResponseEntity<List<Tour>> getFilteredTours(
            @RequestParam("lang") Language lang,
            @RequestParam("type") TourType type) {
        List<Tour> tours = toursService.getFilteredTours(lang, type);
        return ResponseEntity.ok().body(tours);
    }

    /**
     * Returns tour entity by provided id.
     * Test: getTourById
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTourById(@PathVariable("id") Long id) {
        Tour tour;
        try{
            tour = toursService.getTourById(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().body(tour);
    }
}
