package cz.prahousblankou.app;

import cz.prahousblankou.app.service.ImagesService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Profile("!test")
public class RunAfterStartup {
    @Autowired
    ImagesService imagesService;

    final String IMAGES_URL = "https://fit.sherstneva.cz/tjv/assets/images.json";

    @EventListener(ApplicationReadyEvent.class)
    /**
     * When the application starts, we are asking the external source
     * for the list of images we can use and after that
     * we store the list into our database.
     * If there is the same image in our database already, but it is
     * marked as deleted, it wouldn't be imported again.
     */
    public void importImages() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(IMAGES_URL)
                .method("GET", null)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        parseImagesFromResponseAndSave(response);
    }

    private void parseImagesFromResponseAndSave(Response response) throws IOException {
        if (response != null) {
            ResponseBody body = response.body();
            if (body != null) {
                String jsonData = body.string();
                imagesService.importImagesFromExternalSource(jsonData);
            }
        }
    }
}
