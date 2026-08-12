package com.wander.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wander.ReservationStatus;
import com.wander.dto.CreateReservationRequest;
import com.wander.dto.UpdateReservationRequest;
import com.wander.repo.ReservationRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationCacheIntegrationTest extends AbstractIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired StringRedisTemplate stringRedisTemplate;
    @Autowired ReservationRepo reservationRepo;

    private long createReservation() throws Exception {
        var request = new CreateReservationRequest(
                1L, 1L,
                LocalDate.now().plusDays(2L),
                LocalDate.now().plusWeeks(2L),
                ReservationStatus.APPROVED
        );
        String body = mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).get("id").asLong();
    }

    @Test
    void getReservation_populatesCache() throws Exception {
        long id = createReservation();

        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk());

        assertThat(stringRedisTemplate.hasKey("reservations::" + id)).isTrue();
    }

    @Test
    void getReservation_secondCallServedFromCache() throws Exception {
        long id = createReservation();

        // warm the cache
        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk());

        // remove the row directly, bypassing the service/cache entirely
        reservationRepo.deleteById(id);

        // if this still returns 200, the response came from Redis, not Postgres
        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void updateReservation_evictsCache() throws Exception {
        long id = createReservation();

        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk());
        assertThat(stringRedisTemplate.hasKey("reservations::" + id)).isTrue();

        var updateRequest = new UpdateReservationRequest(
                1L,
                1L,
                LocalDate.now().plusDays(3L),
                LocalDate.now().plusWeeks(3L),
                ReservationStatus.APPROVED
        );
        mockMvc.perform(put("/reservation/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        assertThat(stringRedisTemplate.hasKey("reservations::" + id)).isFalse();
    }

    @Test
    void deleteReservation_evictsCache() throws Exception {
        long id = createReservation();

        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk());
        assertThat(stringRedisTemplate.hasKey("reservations::" + id)).isTrue();

        mockMvc.perform(delete("/reservation/delete/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(stringRedisTemplate.hasKey("reservations::" + id)).isFalse();
    }
}