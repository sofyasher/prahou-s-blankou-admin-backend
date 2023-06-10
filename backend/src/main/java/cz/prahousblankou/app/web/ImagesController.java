package cz.prahousblankou.app.web;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.service.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/images")
public class ImagesController {
    @Autowired
    ImagesService imagesService;

    /**
     * Returns all images which are not revoked.
     * test: revokeImagesAndRenewAndGetAllNotRevoked
     */
    @GetMapping()
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imagesService.getAllNotRevoked();
        return ResponseEntity.ok().body(images);
    }
}
