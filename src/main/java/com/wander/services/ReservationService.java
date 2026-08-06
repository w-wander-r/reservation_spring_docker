package com.wander.services;

import com.wander.models.ReservationEntity;
import com.wander.repo.ReservationRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepo reservationRepo;

    public ReservationEntity getReservationById(Long id) {
        if (!reservationRepo.existsById(id)) {
            throw new NoSuchElementException("No such element: " + id);
        }
        return reservationRepo.findById(id).
                orElseThrow(() -> new NoSuchElementException("No such element: " + id));
    }

    public List<ReservationEntity> findAllReservation() {
        return reservationRepo.findAll();
    }

    public ResponseEntity<ReservationEntity> saveReservation(ReservationEntity reservationBody) {
        ReservationEntity reservationEntity = reservationRepo.save(reservationBody);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationEntity);
    }

    public ResponseEntity<ReservationEntity> deleteReservation(Long id) {
        ReservationEntity reservationEntity = reservationRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such element: " + id));

        reservationRepo.delete(reservationEntity);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<ReservationEntity> updateReservation(Long id, ReservationEntity reservationBody) {
        return  ResponseEntity.notFound().build();
    }
}
