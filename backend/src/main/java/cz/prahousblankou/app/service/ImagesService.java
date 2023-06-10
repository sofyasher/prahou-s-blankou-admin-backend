package cz.prahousblankou.app.service;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.exceptions.EntityNotFound;

import java.util.List;

public interface ImagesService {

    List<Image> getAll();

    List<Image> getAllNotRevoked();

    Image getImageById(Long id) throws EntityNotFound;

    void revokeImage(Long id) throws EntityNotFound;

    void renewImage(Long id) throws EntityNotFound;

    void importImagesFromExternalSource(String jsonString);
}
