package com.wander;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    AtomicLong autoID;

    private final Map<Long, Reservation> reservationMap;

    public ReservationService() {
        reservationMap = new HashMap<>();
        autoID = new AtomicLong(0);
    }

    public Reservation getReservationById(Long id) {
        if (!reservationMap.containsKey(id)) {
            throw new NoSuchElementException("No such element: " + id);
        }
        return reservationMap.get(id);
    }

    public List<Reservation> findAllReservation() {
        return reservationMap.values().stream().toList();
    }

    public ResponseEntity<Reservation> saveReservation(Reservation reservationBody) {
        var newReservation = new Reservation(
                autoID.incrementAndGet(),
                reservationBody.userId(),
                reservationBody.roomId(),
                reservationBody.startDate(),
                reservationBody.endDate(),
                ReservationStatus.PENDING
        );
        reservationMap.put(newReservation.id(), newReservation);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newReservation);
    }

    public ResponseEntity<Reservation> deleteReservation(Long id) {
        reservationMap.remove(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<Reservation> updateReservation(Long id,Reservation reservationBody) {
        var newReservation = new Reservation(
                id,
                reservationBody.userId(),
                reservationBody.roomId(),
                reservationBody.startDate(),
                reservationBody.endDate(),
                ReservationStatus.APPROVED
        );

        reservationMap.put(newReservation.id(), newReservation);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newReservation);
    }
}
