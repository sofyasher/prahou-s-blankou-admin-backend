package cz.prahousblankou.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.prahousblankou.app.web.dto.ReservationModel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReservationsControllerTest {

    final String RESERVATIONS_URL = "/api/reservations";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testCreateReservation() throws Exception {
        String json = testReservationData(reservationData());
        mockMvc.perform(
                post(RESERVATIONS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isCreated());
    }


    private static ReservationModel reservationData() {
        ReservationModel reservation = new ReservationModel();
        reservation.setClientName("Petr");
        reservation.setClientEmail("email@email.com");
        reservation.setAdults(2);
        reservation.setChildren(2);
        reservation.setDatetime(1642282721L);
        //reservation from data.sql
        reservation.setTourId(126L);

        return reservation;
    }

    private static String testReservationData(ReservationModel reservation) {
        return "{" + (reservation.getId() != null ? "\"id\": " + reservation.getId() + "," : "") +
                "\"clientName\": \"" + reservation.getClientName() + "\"," +
                "\"clientEmail\": \"" + reservation.getClientEmail() + "\"," +
                "\"adults\": \"" + reservation.getAdults() + "\"," +
                "\"children\": \"" + reservation.getChildren() + "\"," +
                "\"datetime\": \"" + reservation.getDatetime() + "\"," +
                "\"tourId\": \"" + reservation.getTourId() + "\"" +
                "}";
    }
}
