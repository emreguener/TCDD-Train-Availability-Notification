package com.emreguener.TCDD_Ticket.dto;

import java.util.List;

public record ResponseDTO(List<TrainLeg> trainLegs) {

    public record TrainLeg(List<TrainAvailability> trainAvailabilities) {}

    public record TrainAvailability(List<Train> trains) {}

    public record Train(
            int id,
            String number,
            String name,
            String commercialName,
            String type,
            boolean reversed,
            int departureStationId,
            int arrivalStationId,
            List<AvailableFareInfo> availableFareInfo,
            List<TrainSegment> trainSegments
    ) {}

    public record AvailableFareInfo(List<CabinClass> cabinClasses) {}

    public record TrainSegment(
            int departureStationId,
            int arrivalStationId,
            String departureTime,
            String arrivalTime
    ) {}

    public record CabinClass(CabinClassDetail cabinClass, int availabilityCount) {}

    public record CabinClassDetail(int id, String name, String code) {}
}
