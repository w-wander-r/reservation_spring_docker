package com.wander.controllers;

import com.wander.dto.CreateReservationRequest;
import com.wander.dto.ReservationResponse;
import com.wander.dto.UpdateReservationRequest;
import com.wander.services.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/reservation")
@AllArgsConstructor
public class ReservationController {

    private static final Logger log = Logger.getLogger(ReservationController.class.getName());

    private ReservationService reservationService;

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
        log.info("getReservationById called");
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAllReservation() {
        log.info("getAllReservation called");
        return ResponseEntity.ok(reservationService.findAllReservation());
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@Valid @RequestBody CreateReservationRequest  request) {
        log.info("createReservation called");
        ReservationResponse response = reservationService.saveReservation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservation(@PathVariable Long id, @RequestBody UpdateReservationRequest request) {
        log.info("updateReservation called");
        return ResponseEntity.ok(reservationService.updateReservation(id,request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ReservationResponse> deleteReservation(@PathVariable Long id) {
        log.info("deleteReservation called");
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
