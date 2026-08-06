package com.wander;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private static final Logger log = Logger.getLogger(ReservationController.class.getName());

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable("id") Long id) {
        log.info("getReservationById called");
        return reservationService.getReservationById(id);
    }

    @GetMapping("/all")
    public List<Reservation> getAllReservation() {
        log.info("getAllReservation called");
        return reservationService.findAllReservation();
    }

    @PostMapping("/save")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservationBody) {
        log.info("createReservation called");
        return reservationService.saveReservation(reservationBody);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable("id") Long id, @RequestBody Reservation reservation) {
        log.info("updateReservation called");
        return reservationService.updateReservation(id, reservation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable("id") Long id) {
        log.info("deleteReservation called");
        return reservationService.deleteReservation(id);
    }
}
