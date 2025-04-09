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
            List<TrainSegment> trainSegments, // Kalıyor
            List<Segment> segments            // Yeni eklendi – sadece zaman vs.
    ) {}

    public record AvailableFareInfo(List<CabinClass> cabinClasses) {}

    // ↪️ trainSegments: ID’li yapılar
    public record TrainSegment(
            int id,
            int departureStationId,
            int arrivalStationId,
            String departureTime,
            String arrivalTime
    ) {}

    // Yeni: Zamanlı segment yapısı
    public record Segment(
            int id,
            int departureStationId,
            int arrivalStationId,
            long departureTime,  // epoch millis
            long arrivalTime
    ) {}

    public record CabinClass(CabinClassDetail cabinClass, int availabilityCount) {}

    public record CabinClassDetail(int id, String name, String code) {}
}
