package cz.prahousblankou.app.web;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/admin/images")
public class ImagesAdminController {
    @Autowired
    ImagesService imagesService;

    /**
     * Sets to the image with provided id flag deleted, so the image wouldn't
     * be shown in the offer.
     * test: revokeImagesAndRenewAndGetAllNotRevoked
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> revokeImage(@PathVariable("id") Long id) {
        try {
            imagesService.revokeImage(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Sets to the image with provided flag deleted to false, so the image wouldn't
     * be shown in the offer.
     * test: revokeImagesAndRenewAndGetAllNotRevoked
     */
    @PostMapping("/{id}/renew")
    public ResponseEntity<Object> renewImage(@PathVariable("id") Long id) {
        try {
            imagesService.renewImage(id);
        } catch (EntityNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Returns all images.
     * test: TODO
     */
    @GetMapping()
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imagesService.getAll();
        return ResponseEntity.ok().body(images);
    }
}
