package com.wander.services;

import com.wander.dto.CreateReservationRequest;
import com.wander.dto.ReservationResponse;
import com.wander.dto.UpdateReservationRequest;
import com.wander.mapper.ReservationMapper;
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
    private ReservationMapper reservationMapper;

    public ReservationResponse getReservationById(Long id) {
        return reservationRepo.findById(id)
                .map(reservationMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
    }

    public List<ReservationResponse> findAllReservation() {
        return reservationRepo.findAll()
                .stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    public ReservationResponse saveReservation(CreateReservationRequest request) {
        ReservationEntity entityToSave = reservationMapper.toEntity(request);
        ReservationEntity saveEntity = reservationRepo.save(entityToSave);
        return reservationMapper.toResponse(saveEntity);
    }

    public void deleteReservation(Long id) {
        if (!reservationRepo.existsById(id)) {
            throw new NoSuchElementException("Reservation not found");
        }
        reservationRepo.deleteById(id);
    }

    public ReservationResponse updateReservation(Long id, UpdateReservationRequest request) {
        ReservationEntity existingEntity = reservationRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
        reservationMapper.updateEntityFromDto(request, existingEntity);
        reservationRepo.save(existingEntity);
        return reservationMapper.toResponse(existingEntity);
    }
}
