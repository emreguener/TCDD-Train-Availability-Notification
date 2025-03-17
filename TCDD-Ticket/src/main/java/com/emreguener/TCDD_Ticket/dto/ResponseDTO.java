package com.emreguener.TCDD_Ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private List<TrainLeg> trainLegs;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainLeg {
        private List<TrainAvailability> trainAvailabilities;  
        private int resultCount;
    }
    
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainAvailability {
        private List<Train> trains;
        private int totalTripTime;
        private double minPrice;
        private boolean connection;
        private boolean dayChanged;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Train {
        private int id;
        private String number;
        private String name;
        private String commercialName;
        private String type;
        private boolean reversed;
        private int departureStationId;
        private int arrivalStationId;
        private List<AvailableFareInfo> availableFareInfo;
        private List<TrainSegment> trainSegments;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AvailableFareInfo {
        private List<CabinClass> cabinClasses;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TrainSegment {
        private int departureStationId;
        private int arrivalStationId;
        private String departureTime;
        private String arrivalTime;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CabinClass {
        private CabinClassDetail cabinClass;
        private int availabilityCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CabinClassDetail {
        private int id;
        private String name;
        private String code;
    }
}
