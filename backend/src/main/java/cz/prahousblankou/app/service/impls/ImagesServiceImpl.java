package cz.prahousblankou.app.service.impls;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.domain.repository.ImageRepository;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import cz.prahousblankou.app.service.ImagesService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ImagesServiceImpl implements ImagesService {
    @Autowired
    ImageRepository imageRepository;

    @Override
    public List<Image> getAll() {
        return imageRepository.findAll();
    }

    @Override
    public List<Image> getAllNotRevoked() {
        return imageRepository.getAllByDeleted(false);
    }

    @Override
    public Image getImageById(Long id) throws EntityNotFound {
        Optional<Image> image = imageRepository.findById(id);
        if (image.isEmpty()) {
            throw new EntityNotFound("Image with provided ID doesn't exist.");
        }

        return image.get();
    }

    @Override
    @Transactional
    public void revokeImage(Long id) throws EntityNotFound {
        // check for existence in database
        Image image = getImageById(id);
        imageRepository.revokeImage(id);
    }

    @Override
    @Transactional
    public void renewImage(Long id) throws EntityNotFound {
        // check for existence in database
        Image image = getImageById(id);
        imageRepository.renewImage(id);
    }

    @Override
    public void importImagesFromExternalSource(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(jsonString);
            JSONObject imagesJson = (JSONObject) obj;
            JSONArray images = (JSONArray) imagesJson.get("images");

            images.forEach((img) -> {
                if (imageRepository.getByUrl((String) img) == null) {
                    Image image = new Image();
                    image.setUrl((String) img);
                    imageRepository.save(image);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
