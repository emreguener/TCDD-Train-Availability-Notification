package com.emreguener.TCDD_Ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record RequestDTO(
        String searchType,
        boolean searchReservation,
        List<PassengerTypeCount> passengerTypeCounts,
        List<SearchRoutes> searchRoutes
) {
    public record PassengerTypeCount(int id, int count) {}

    public record SearchRoutes(
            int departureStationId,
            String departureStationName,
            int arrivalStationId,
            String arrivalStationName,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
            LocalDateTime departureDate
    ) {}
}
