package com.emreguener.TCDD_Ticket.dto;

import lombok.Data;
import java.util.List;

@Data
public class ResponseDTO {
    private List<TrainLeg> trainLegs;
    private int legCount;
    private int roundTripDiscount;
    private long maxRegionalTrainsRoundTripDays;

    public record TrainLeg(List<TrainAvailability> trainAvailabilities, int resultCount) {}
    public record TrainAvailability(List<Train> trains, int totalTripTime, double minPrice, boolean connection, boolean dayChanged) {}
    public record Train(int id, String number, String name, String commercialName, String type, String line, boolean reversed, int scheduleId,
                        int departureStationId, int arrivalStationId, List<AvailableFareInfo> availableFareInfo, List<TrainSegment> trainSegments) {}
    public record AvailableFareInfo(List<CabinClass> cabinClasses) {}
    public record TrainSegment(int departureStationId, int arrivalStationId, String departureTime, String arrivalTime) {}
    public record CabinClass(CabinClassDetail cabinClass, int availabilityCount) {}
    public record CabinClassDetail(int id, String name, String code) {}
}
