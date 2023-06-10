package cz.prahousblankou.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.prahousblankou.app.domain.entity.Reservation;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReservationsAdminControllerTest {

    final String RESERVATIONS_ADMIN_URL = "/api/admin/reservations";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testUpdateReservation() throws Exception {
        String json = reservationDataJson(reservationData());
        MvcResult result = mockMvc.perform(
                        put(RESERVATIONS_ADMIN_URL + "/202")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Reservation response = objectMapper.readValue(responseAsString, Reservation.class);
        assertEquals(response.getClientName(), "Jan Novak");
        assertEquals(response.getTour().getId(), 124L);
    }

    @Test
    void testGetAllReservations() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(RESERVATIONS_ADMIN_URL)
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Reservation[] response = objectMapper.readValue(responseAsString, Reservation[].class);
        assertTrue(response.length > 2);
    }

    @Test
    void testGetReservationById() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(RESERVATIONS_ADMIN_URL + "/201")
                ).andExpect(status().isOk())
                .andReturn();

        String responseAsString = result.getResponse().getContentAsString();
        Reservation response = objectMapper.readValue(responseAsString, Reservation.class);
        assertEquals(response.getClientName(), "Petr");
        assertEquals(response.getClientEmail(), "petr@gmail.com");
    }

    @Test
    void testDeleteReservation() throws Exception {
        // reservation from data.sql
        final String RESERVATION_URL = RESERVATIONS_ADMIN_URL + "/203";
        mockMvc.perform(
                delete(RESERVATION_URL)
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(
                get(RESERVATION_URL)
        ).andExpect(status().isNotFound()).andReturn();
    }

    private static ReservationModel reservationData() {
        // updates reservation from data.sql
        ReservationModel reservation = new ReservationModel();
        reservation.setId(202L);
        reservation.setClientEmail("honza@gmail.com");
        reservation.setAdults(2);
        reservation.setChildren(1);
        reservation.setDatetime(1647689461L);

        reservation.setClientName("Jan Novak");
        reservation.setTourId(124L);

        return reservation;
    }

    private static String reservationDataJson(ReservationModel reservation) {
        return "{" + (reservation.getId() != null ? "\"id\": " + reservation.getId() + "," : "") +
                "\"clientName\": \"" + reservation.getClientName() + "\"," +
                "\"clientEmail\": \"" + reservation.getClientEmail() + "\"," +
                "\"adults\": \"" + reservation.getAdults() + "\"," +
                "\"children\": \"" + reservation.getChildren() + "\"," +
                "\"datetime\": \"" + reservation.getDatetime() + "\"," +
                "\"tourId\": " + reservation.getTourId() + "" +
                "}";
    }
}
