package com.emreguener.TCDD_Ticket.service;

import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Test
    public void testGetTrain() {
        LocalDateTime gidisTarih = LocalDateTime.of(2025, 3, 24, 19, 0);
        LocalDateTime gidisTarihSon = LocalDateTime.of(2025, 3, 24, 22, 0);

        Set<Train> result = ticketService.getTrain(gidisTarih, gidisTarihSon,
                "Eskişehir", "İstanbul (Söğütlüçeşme)",
                93, 1325, "EKONOMİ");

        // Eğer en az bir tren varsa, test başarılıdır
        assertFalse(result.isEmpty());
    }
}
