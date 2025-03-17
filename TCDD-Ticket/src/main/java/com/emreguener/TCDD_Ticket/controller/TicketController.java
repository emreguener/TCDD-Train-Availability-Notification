package com.emreguener.TCDD_Ticket.controller;

import com.emreguener.TCDD_Ticket.dto.SeferDTO;
import com.emreguener.TCDD_Ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/search")
    @Operation(summary = "Belirli kriterlere göre tren seferlerini getirin ve boş koltuk varsa e-posta ile bildirin.", 
               description = "Girilen parametrelere göre uygun tren seferlerini listeler. Eğer boş koltuk varsa e-posta gönderilir.")
    public List<SeferDTO> getTrain(
            @Parameter(description = "Gidiş tarihi", required = true)
            @RequestParam LocalDateTime gidisTarih,

            @Parameter(description = "Gidiş tarihi sonu", required = true)
            @RequestParam LocalDateTime gidisTarihSon,

            @Parameter(description = "Biniş istasyonu adı", required = true)
            @RequestParam String binisIstasyonu,

            @Parameter(description = "Varış istasyonu adı", required = true)
            @RequestParam String inisIstasyonu,

            @Parameter(description = "Biniş istasyonu ID", required = true)
            @RequestParam int binisIstasyonId,

            @Parameter(description = "İniş istasyonu ID", required = true)
            @RequestParam int inisIstasyonId,

            @Parameter(description = "Koltuk tipi", required = true)
            @RequestParam String koltukTipi,

            @Parameter(description = "E-posta adresi (Opsiyonel, boş koltuk varsa mail atılır)")
            @RequestParam(required = false) String email
            
    ) {
        // Servis çağrısı ile trenleri getir
        List<SeferDTO> trenListesi = ticketService.getTrain(gidisTarih, gidisTarihSon, binisIstasyonu, inisIstasyonu, binisIstasyonId, inisIstasyonId, koltukTipi, email)
                .stream().map(train -> new SeferDTO(
                        train.getName(),
                        train.getTrainSegments().get(0).getDepartureTime(),
                        binisIstasyonu,
                        inisIstasyonu,
                        koltukTipi,
                        train.getAvailableFareInfo().get(0).getCabinClasses().get(0).getAvailabilityCount()
                )).toList();

        // JSON yanıt olarak döndür
        return trenListesi;
    }
}
