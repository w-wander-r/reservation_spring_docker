package com.wander.dto;

import com.wander.ReservationStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateReservationRequest(
        @NotNull Long userId,
        @NotNull Long roomId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull ReservationStatus status
) {}

