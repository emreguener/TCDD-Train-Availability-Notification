package com.emreguener.TCDD_Ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
    private String searchType;
    private boolean searchReservation;
    private List<PassengerTypeCount> passengerTypeCounts;
    private List<SearchRoutes> searchRoutes;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PassengerTypeCount {
        private int id;
        private int count;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SearchRoutes {
        private int departureStationId;
        private String departureStationName;
        private int arrivalStationId;
        private String arrivalStationName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        private LocalDateTime departureDate;
    }
}
