package cz.prahousblankou.app;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class RunAfterStartupTest {

    final String IMAGES_URL = "https://fit.sherstneva.cz/tjv/assets/images.json";

    @Test
    void resourceWithImagesIsAvailableTest() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(IMAGES_URL)
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        assertEquals(response.code(), 200);
    }
}
