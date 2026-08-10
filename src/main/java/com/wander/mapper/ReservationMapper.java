package com.wander.mapper;

import com.wander.dto.CreateReservationRequest;
import com.wander.dto.ReservationResponse;
import com.wander.dto.UpdateReservationRequest;
import com.wander.models.ReservationEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReservationMapper {

    // DTO -> Entity
    @Mapping(target = "id", ignore = true)
    ReservationEntity toEntity(CreateReservationRequest request);

    // Entity -> response
    ReservationResponse toResponse(ReservationEntity reservationEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(UpdateReservationRequest request, @MappingTarget ReservationEntity entity);
}
