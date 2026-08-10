package com.wander.services;

import com.wander.ReservationStatus;
import com.wander.dto.CreateReservationRequest;
import com.wander.dto.ReservationResponse;
import com.wander.dto.UpdateReservationRequest;
import com.wander.mapper.ReservationMapper;
import com.wander.models.ReservationEntity;
import com.wander.repo.ReservationRepo;
import com.wander.validation.ReservationConflictException;
import lombok.AllArgsConstructor;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ReservationService {

    private ReservationRepo reservationRepo;
    private ReservationMapper reservationMapper;

    @Transactional
    public ReservationResponse getReservationById(Long id) {
        return reservationRepo.findById(id)
                .map(reservationMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));
    }

    @Transactional
    public Page<ReservationResponse> findAllReservation(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return reservationRepo.findAll(pageable)
                .map(reservationMapper::toResponse);
    }

    @Transactional
    public ReservationResponse saveReservation(CreateReservationRequest request) {
        if (!request.endDate().isAfter(request.startDate())) {
            throw new IllegalArgumentException("End date should be after start date");
        }
        boolean isConflict = reservationRepo.existsOverlappingReservation(
                request.roomId(),
                request.startDate(),
                request.endDate(),
                ReservationStatus.CANCELLED
        );
        if (isConflict) {
            throw new ReservationConflictException("Reservation already exists");
        }
        ReservationEntity entityToSave = reservationMapper.toEntity(request);
        ReservationEntity saveEntity = reservationRepo.save(entityToSave);
        return reservationMapper.toResponse(saveEntity);
    }

    @Transactional
    public void deleteReservation(Long id) {
        if (!reservationRepo.existsById(id)) {
            throw new NoSuchElementException("Reservation not found");
        }
        reservationRepo.deleteById(id);
    }

    @Retryable(
            retryFor = { OptimisticLockingFailureException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional
    public ReservationResponse updateReservation(Long id, UpdateReservationRequest request) {
        ReservationEntity existingEntity = reservationRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Reservation not found"));

        if (!request.endDate().isAfter(request.startDate())) {
            throw new IllegalArgumentException("End date should be after start date");
        }

        boolean isConflict = reservationRepo.existsOverlappingReservationExcludingSelf(
                request.roomId(),
                id,
                request.startDate(),
                request.endDate(),
                ReservationStatus.CANCELLED
        );

        if (isConflict) {
            throw new ReservationConflictException("Reservation already exists");
        }

        reservationMapper.updateEntityFromDto(request, existingEntity);
        reservationRepo.saveAndFlush(existingEntity);
        return reservationMapper.toResponse(existingEntity);
    }
}
