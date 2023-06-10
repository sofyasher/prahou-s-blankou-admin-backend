package cz.prahousblankou.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.prahousblankou.app.domain.entity.Tour;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ToursControllerTest {

    final String TOURS_URL = "/api/tours";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getToursByTypeAndLanguage() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(TOURS_URL + "?lang=CZ&type=CLASSIC")
        ).andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(TOURS_URL + "?lang=CZ&type=UNDISCOVERED")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void getTourById() throws Exception {
        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders
                                .get(TOURS_URL + "/125")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Tour response = objectMapper.readValue(responseAsString, Tour.class);
        assertEquals(response.getName(), "kkkk");
    }

}
