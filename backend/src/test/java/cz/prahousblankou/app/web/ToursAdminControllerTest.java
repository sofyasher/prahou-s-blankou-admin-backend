package cz.prahousblankou.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.prahousblankou.app.domain.entity.Tour;
import cz.prahousblankou.app.domain.entity.enums.Language;
import cz.prahousblankou.app.domain.entity.enums.MeetingPoint;
import cz.prahousblankou.app.domain.entity.enums.TourType;
import cz.prahousblankou.app.web.dto.TourModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ToursAdminControllerTest {

    final String TOURS_ADMIN_URL = "/api/admin/tours";
    final String TOURS_PUBLIC_URL = "/api/tours";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetToursForAdminInCzechAndEnglish() throws Exception {
        mockMvc.perform(
                get(TOURS_ADMIN_URL + "?lang=CZ")
        ).andExpect(status().isOk());

        mockMvc.perform(get(TOURS_ADMIN_URL + "?lang=EN")
        ).andExpect(status().isOk());
    }

    @Test
    void testGetToursForAdminInUnknownLanguage() throws Exception {
        mockMvc.perform(get(TOURS_ADMIN_URL + "?lang=RU")
        ).andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTour() throws Exception {
        String json = tourDataJson(tourData());
        mockMvc.perform(
                post(TOURS_ADMIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());
    }

    @Test
    void testCreateUndUpdateTour() throws Exception {
        TourModel tour = tourData();
        String json = tourDataJson(tourData());
        MvcResult result = mockMvc.perform(
                        post(TOURS_ADMIN_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isCreated())
                .andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Tour response = objectMapper.readValue(responseAsString, Tour.class);

        tour.setId(response.getId());
        tour.setName("the best tour");
        tour.setLang(Language.EN);
        tour.setPrice("75 EUR");

        json = tourDataJson(tour);

        mockMvc.perform(
                put(TOURS_ADMIN_URL + "/" + response.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isOk());
    }

    @Test
    void testDeleteTour() throws Exception {
        // tour from data.sql
        final String TOUR_TO_DELETE_URL = TOURS_ADMIN_URL + "/123";
        final String TOUR_TO_GET_URL = TOURS_PUBLIC_URL + "/123";
        mockMvc.perform(
                delete(TOUR_TO_DELETE_URL)
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get(TOUR_TO_GET_URL)
        ).andExpect(status().isOk()).andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Tour response = objectMapper.readValue(responseAsString, Tour.class);
        assertTrue(response.isDeleted());
    }

    @Test
    void testRenewTour() throws Exception {
        // tour from data.sql
        final String TOUR_TO_RENEW_URL = TOURS_ADMIN_URL + "/127" + "/renew";
        final String TOUR_TO_GET_URL = TOURS_PUBLIC_URL + "/127";
        mockMvc.perform(
                post(TOUR_TO_RENEW_URL)
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get(TOUR_TO_GET_URL)
        ).andExpect(status().isOk()).andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Tour response = objectMapper.readValue(responseAsString, Tour.class);
        assertFalse(response.isDeleted());
    }

    @Test
    /**
     * the test must be run as the last, because all the tours are deleted from the database
     * during the test
     */
    void testImportTours() throws Exception {
        mockMvc.perform(post(TOURS_ADMIN_URL + "/import")).andExpect(status().isCreated());
        MvcResult resultEN = mockMvc.perform(
                        get(TOURS_ADMIN_URL + "?lang=EN")
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString = resultEN.getResponse().getContentAsString();
        Tour[] response = objectMapper.readValue(responseAsString, Tour[].class);
        assertTrue(response.length > 15);

        MvcResult resultCZ = mockMvc.perform(
                        get(TOURS_ADMIN_URL + "?lang=CZ")
                ).andExpect(status().isOk())
                .andReturn();

        String responseCZAsString = resultCZ.getResponse().getContentAsString();
        Tour[] responseCZ = objectMapper.readValue(responseCZAsString, Tour[].class);
        assertTrue(responseCZ.length > 15);
    }

    private static TourModel tourData() {
        TourModel tour = new TourModel();
        tour.setName("nejlepsi prochazka");
        tour.setLegend("lepsi prochazku jste jeste nezazili");
        tour.setDescription("nejlepsi prichazka na svete");
        tour.setDescription("nejlepsi prichazka na svete");
        tour.setType(TourType.CLASSIC);
        tour.setPrice("1500 CZK");
        tour.setDuration(5);
        tour.setLang(Language.CZ);
        tour.setMeetingPoint(MeetingPoint.MS);
        Set<Long> imagesIds = new HashSet<>();
        imagesIds.add(1L);
        imagesIds.add(2L);
        imagesIds.add(3L);
        tour.setImageIds(imagesIds);

        return tour;
    }

    private static String tourDataJson(TourModel tour) {
        return "{" + (tour.getId() != null ? "\"id\": " + tour.getId() + "," : "") +
                "\"name\": \"" + tour.getName() + "\"," +
                "\"legend\": \"" + tour.getLegend() + "\"," +
                "\"description\": \"" + tour.getDescription() + "\"," +
                "\"type\": \"" + tour.getType() + "\"," +
                "\"price\": \"" + tour.getPrice() + "\"," +
                "\"duration\": \"" + tour.getDuration() + "\"," +
                "\"lang\": \"" + tour.getLang() + "\"," +
                "\"meetingPoint\": \"" + tour.getMeetingPoint() + "\"," +
                "\"imageIds\": " + tour.getImageIds().toString() + "" +
                "}";
    }
}
