package com.wander.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.wander.ReservationStatus;
import com.wander.dto.CreateReservationRequest;
import com.wander.dto.UpdateReservationRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationIntegrationTest extends AbstractIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void fullCrudFlow() throws Exception {
        var request = new CreateReservationRequest(
                1L,
                1L,
                LocalDate.now().plusDays(2L),
                LocalDate.now().plusWeeks(2L),
                ReservationStatus.APPROVED
        );

        // CREATE
        MvcResult createResult = mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode created = objectMapper.readTree(createResult.getResponse().getContentAsString());
        Long id = created.get("id").asLong();

        // READ
        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("APPROVED")));

        // UPDATE
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

        // LIST (returns a Page, so assert on $.content)
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));

        // DELETE
        mockMvc.perform(delete("/reservation/delete/{id}", id))
                .andExpect(status().isNoContent());

        // VERIFY GONE
        mockMvc.perform(get("/reservation/{id}", id))
                .andExpect(status().isNotFound());
    }
}