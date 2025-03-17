package com.emreguener.TCDD_Ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestDTO {
    private String searchType = "DOMESTIC";
    private boolean searchReservation = false;
    private List<PassengerTypeCount> passengerTypeCounts;
    private List<SearchRoutes> searchRoutes;

    public static record PassengerTypeCount(int id, int count) {}
    public static record SearchRoutes(int departureStationId, String departureStationName,
                                      int arrivalStationId, String arrivalStationName,
                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
                                      LocalDateTime departureDate) {}
}
