package com.emreguener.TCDD_Ticket.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.emreguener.TCDD_Ticket.dto.SeferDTO;
import com.emreguener.TCDD_Ticket.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/trains")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/search")
    @Operation(summary = "Tren seferlerini getirir ve boş koltuk varsa e-posta ile bildirir.", 
               description = "Belirtilen parametrelere göre trenleri listeler ve boş koltuk varsa e-posta gönderilir.")
    public Page<SeferDTO> getTrain(
            @RequestParam LocalDateTime gidisTarih,
            @RequestParam LocalDateTime gidisTarihSon,
            @RequestParam String binisIstasyonu,
            @RequestParam String inisIstasyonu,
            @RequestParam int binisIstasyonId,
            @RequestParam int inisIstasyonId,
            @RequestParam String koltukTipi,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ticketService.getTrain(gidisTarih, gidisTarihSon, binisIstasyonu, inisIstasyonu, binisIstasyonId, inisIstasyonId, koltukTipi, email, page, size);
    }
}
