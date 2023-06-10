package cz.prahousblankou.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.prahousblankou.app.domain.entity.Image;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ImagesControllerTest {

    final String IMAGES_PUBLIC_URL = "/api/images";
    final String IMAGES_ADMIN_URL = "/api/admin/images";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void revokeImagesAndRenewAndGetAllNotRevoked() throws Exception {
        MvcResult result1 = mockMvc.perform(
                        get(IMAGES_PUBLIC_URL)
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString1 = result1.getResponse().getContentAsString();
        Image[] response1 = objectMapper.readValue(responseAsString1, Image[].class);
        int count = response1.length;

        Integer[] idxsToRevoke = {0, 3, 5, 8};
        Long[] idsToRevoke = Stream.of(idxsToRevoke).map((index) -> response1[index].getId()).toArray(Long[]::new);
        Arrays.stream(idsToRevoke).iterator().forEachRemaining(id -> {
            try {
                mockMvc.perform(
                        delete(IMAGES_ADMIN_URL + "/" + id)
                ).andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MvcResult result2 = mockMvc.perform(
                        get(IMAGES_PUBLIC_URL)
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString2 = result2.getResponse().getContentAsString();
        Image[] response2 = objectMapper.readValue(responseAsString2, Image[].class);
        int countAfterRevoke = response2.length;

        assertEquals(count - countAfterRevoke, 4);

        Arrays.stream(idsToRevoke).limit(2).forEach(id -> {
            try {
                mockMvc.perform(
                        post(IMAGES_ADMIN_URL + "/" + id + "/renew")
                ).andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        MvcResult result3 = mockMvc.perform(
                        get(IMAGES_PUBLIC_URL)
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString3 = result3.getResponse().getContentAsString();
        Image[] response3 = objectMapper.readValue(responseAsString3, Image[].class);
        int countAfterRenew = response3.length;

        assertEquals(countAfterRenew - countAfterRevoke, 2);
    }

    @Test
    void getAllImagesTest() throws Exception {
        mockMvc.perform(
                get(IMAGES_ADMIN_URL)
        ).andExpect(status().isOk());
    }
}
