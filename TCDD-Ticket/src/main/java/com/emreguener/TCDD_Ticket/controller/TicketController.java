package com.emreguener.TCDD_Ticket.controller;

import com.emreguener.TCDD_Ticket.dto.RequestDTO;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import com.emreguener.TCDD_Ticket.service.TicketService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/trains")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/filtered")
    public Set<Train> getTrain(@RequestBody RequestDTO requestDTO) {

        // Gelen JSON'un doğruluğunu kontrol et
        if (requestDTO == null || requestDTO.getSearchRoutes() == null || requestDTO.getSearchRoutes().isEmpty()) {
            throw new IllegalArgumentException("❌ Hata: searchRoutes alanı boş olamaz!");
        }

        // JSON'dan gelen tarihleri LocalDateTime formatına çevir
        LocalDateTime startTime = requestDTO.getSearchRoutes().get(0).getDepartureDate();
        LocalDateTime endTime = startTime.plusHours(3); // Örnek olarak 3 saat ekleyelim

        return ticketService.getTrain(
                startTime,
                endTime,
                requestDTO.getSearchRoutes().get(0).getDepartureStationName(),
                requestDTO.getSearchRoutes().get(0).getArrivalStationName(),
                requestDTO.getSearchRoutes().get(0).getDepartureStationId(),
                requestDTO.getSearchRoutes().get(0).getArrivalStationId(),
                "EKONOMİ"
        );
    }

}
