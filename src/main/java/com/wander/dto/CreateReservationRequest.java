package com.wander.dto;

import com.wander.ReservationStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

public record CreateReservationRequest(
        @Positive(message = "userID should be positive")
        @NotNull(message = "userID should not be null")
        Long userId,

        @Positive(message = "roomID should be positive")
        @NotNull(message = "roomID should not be null")
        Long roomId,

        @Future(message = "Start date must be in future")
        @NotNull(message = "Start date should not be null")
        LocalDate startDate,

        @NotNull(message = "End date should not be null")
        LocalDate endDate,

        @NotNull(message = "Status should not be null")
        ReservationStatus status
) {}

