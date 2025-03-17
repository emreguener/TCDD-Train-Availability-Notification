package com.emreguener.TCDD_Ticket.controller;

import com.emreguener.TCDD_Ticket.dto.RequestDTO;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import com.emreguener.TCDD_Ticket.service.RestClientService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/trains")
public class TicketController {

    private final RestClientService restClientService;

    public TicketController(RestClientService restClientService) {
        this.restClientService = restClientService;
    }

    @PostMapping("/filtered")
    public Set<Train> getFilteredTrains(
            @RequestBody RequestDTO requestDTO,
            @RequestParam String cabinType,
            @RequestParam String startTime,
            @RequestParam String endTime) {

        return restClientService.getFilteredTrains(requestDTO, cabinType, startTime, endTime);
    }
}
