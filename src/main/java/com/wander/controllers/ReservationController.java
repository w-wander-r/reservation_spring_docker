package com.wander.controllers;

import com.wander.models.ReservationEntity;
import com.wander.services.ReservationService;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.Audited;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ReservationEntity getReservationById(@PathVariable("id") Long id) {
        log.info("getReservationById called");
        return reservationService.getReservationById(id);
    }

    @GetMapping("/all")
    public List<ReservationEntity> getAllReservation() {
        log.info("getAllReservation called");
        return reservationService.findAllReservation();
    }

    @PostMapping("/save")
    public ResponseEntity<ReservationEntity> createReservation(@RequestBody ReservationEntity reservationBody) {
        log.info("createReservation called");
        return reservationService.saveReservation(reservationBody);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReservationEntity> updateReservation(@PathVariable("id") Long id, @RequestBody ReservationEntity reservation) {
        log.info("updateReservation called");
        return reservationService.updateReservation(id, reservation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ReservationEntity> deleteReservation(@PathVariable("id") Long id) {
        log.info("deleteReservation called");
        return reservationService.deleteReservation(id);
    }
}
