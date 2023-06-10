package cz.prahousblankou.app.service;

import cz.prahousblankou.app.domain.entity.Image;
import cz.prahousblankou.app.domain.repository.ImageRepository;
import cz.prahousblankou.app.exceptions.EntityNotFound;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class ImagesServiceTest {

    @Autowired
    ImagesService imagesService;

    @MockBean
    ImageRepository imageRepository;

    @Captor
    ArgumentCaptor<Image> imageCaptor;

    @Test
    void getAllImagesTest() {
        when(imageRepository.findAll()).thenReturn(images());
        List<Image> images = imagesService.getAll();
        assertEquals(images, images());
    }

    @Test
    void getAllNotRevokedTest() {
        when(imageRepository.getAllByDeleted(false)).thenReturn(images());
        List<Image> images = imagesService.getAllNotRevoked();
        assertEquals(images, images());
    }

    @Test
    void getImageByIdTest() throws EntityNotFound {
        when(imageRepository.findById(12L)).thenReturn(Optional.of(images().get(2)));
        Image image = imagesService.getImageById(12L);
        assertEquals(image, images().get(2));
    }

    @Test
    void getImageByBadIdTest() {
        when(imageRepository.findById(895L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFound.class, () -> imagesService.getImageById(895L));
    }

    @Test
    void revokeImageTest() throws EntityNotFound {
        when(imageRepository.findById(10L)).thenReturn(Optional.of(images().get(0)));
        imagesService.revokeImage(10L);
        verify(imageRepository).revokeImage(10L);
    }

    @Test
    void renewImageTest() throws EntityNotFound {
        when(imageRepository.findById(10L)).thenReturn(Optional.of(images().get(0)));
        imagesService.renewImage(10L);
        verify(imageRepository).renewImage(10L);
    }

    @Test
    void importImagesTest() {
        when(imageRepository.getByUrl(any())).thenReturn(null);
        imagesService.importImagesFromExternalSource(imagesJson());
        verify(imageRepository, times(4)).save(imageCaptor.capture());
        Stream<Object> urls = imageCaptor.getAllValues().stream().map(Image::getUrl);
        assertEquals(urls.collect(Collectors.toList()), imagesUrls());
    }

    private static List<String> imagesUrls() {
        return List.of(new String[]{
                "images/prague/Andel_sm.jpg", "images/prague/CharlesBridge.JPG", "images/prague/CharlesIV.jpg", "images/prague/Hradcany_sm.jpg"
        });
    }

    private static String imagesJson() {
        return "{\"images\": [\"images/prague/Andel_sm.jpg\",\"images/prague/CharlesBridge.JPG\",\"images/prague/CharlesIV.jpg\", \"images/prague/Hradcany_sm.jpg\"]}";
    }

    private static List<Image> images() {
        return List.of(new Image[]{
                new Image(10L, "images/aaa.png", false),
                new Image(11L, "images/bbb.png", true),
                new Image(12L, "images/ddd.jpeg", false),
                new Image(13L, "images/jjj.jpeg", true)
        });
    }
}
