package com.wander.dto;

import com.wander.ReservationStatus;

import java.time.LocalDate;

public record UpdateReservationRequest(
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {}
