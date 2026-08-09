package com.wander.repo;

import com.wander.ReservationStatus;
import com.wander.models.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<ReservationEntity, Long> {
    @Query("""
        SELECT COUNT(r) > 0 FROM ReservationEntity r
        WHERE r.roomId = :roomId
        AND r.status != :cancelledStatus
        AND r.startDate < :endDate
        AND r.endDate > :startDate
    """)
    boolean existsOverlappingReservation(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );

    @Query("""
        SELECT COUNT(r) > 0 FROM ReservationEntity r
        WHERE r.roomId = :roomId
        AND r.id != :currentReservationId
        AND r.status != :cancelledStatus
        AND r.startDate < :endDate
        AND r.endDate > :startDate
    """)
    boolean existsOverlappingReservationExcludingSelf(
            @Param("roomId") Long roomId,
            @Param("currentReservationId") Long currentReservationId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("cancelledStatus") ReservationStatus cancelledStatus
    );
}
