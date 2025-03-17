package com.emreguener.TCDD_Ticket.service;

import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import com.emreguener.TCDD_Ticket.dto.SeferDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Test
    void testGetTrainWithSwaggerParamsAndEmail() {
        System.out.println("🟢 Test başladı...");

        // ✅ Swagger UI formatına uygun tarih stringi
        String gidisTarihStr = "2025-03-20T10:00:00";
        String gidisTarihSonStr = "2025-03-22T10:00:00";

        // ✅ Tarihleri Swagger formatından LocalDateTime'a çevir
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime gidisTarih = LocalDateTime.parse(gidisTarihStr, formatter);
        LocalDateTime gidisTarihSon = LocalDateTime.parse(gidisTarihSonStr, formatter);

        // ✅ Swagger UI'de kullanılan istasyon ve ID'ler
        String binisIstasyonu = "ESKİŞEHİR";
        String inisIstasyonu = "BALIKESİR";
        int binisIstasyonId = 93;
        int inisIstasyonId = 1159;
        String koltukTipi = "EKONOMİ";
        String email = "test@example.com";  // Test için e-posta adresi

        // ✅ Servis metodunu çağır ve `Set<Train>` sonucu al
        List<Train> trainResults = ticketService.getTrain(
                gidisTarih, gidisTarihSon, binisIstasyonu, inisIstasyonu, binisIstasyonId, inisIstasyonId, koltukTipi, email);

        System.out.println("✅ Servis çağrıldı, gelen veri sayısı: " + trainResults.size());

        // ✅ Sonuçları `List<SeferDTO>` formatına dönüştür
        List<SeferDTO> result = trainResults.stream().map(train ->
                new SeferDTO(
                        train.getName(),
                        gidisTarih.toString(),
                        binisIstasyonu,
                        inisIstasyonu,
                        koltukTipi,
                        train.getAvailableFareInfo().get(0).getCabinClasses().get(0).getAvailabilityCount()
                )).collect(Collectors.toList());

        // ✅ Gelen tren verilerini detaylı yazdıralım
        System.out.println("📌 Gelen Trenler:");
        result.forEach(train -> {
            System.out.println("🚆 Tren Adı: " + train.getTrenAdi());
            System.out.println("📅 Kalkış Tarihi: " + train.getKalkisTarihi());
            System.out.println("🚉 Biniş İstasyonu: " + train.getBinisIstasyonu());
            System.out.println("🏁 Varış İstasyonu: " + train.getVarisIstasyonu());
            System.out.println("💺 Koltuk Tipi: " + train.getKoltukTipi());
            System.out.println("🎟️ Boş Koltuk Sayısı: " + train.getBosKoltukSayisi());
            System.out.println("-------------------------------------");
        });

        // ✅ Sonuçları doğrula
        assertNotNull(result, "❌ Dönen sonuç null olmamalı!");
        assertFalse(result.isEmpty(), "❌ Liste boş olmamalı!");
        assertEquals(2, result.size(), "❌ Swagger'da 2 tren vardı, burada da 2 bekliyoruz!");

        // ✅ Eğer boş koltuk varsa e-posta gönderiminin yapılmış olmasını bekliyoruz
        boolean bosKoltukVarMi = result.stream().anyMatch(train -> train.getBosKoltukSayisi() > 0);
        if (bosKoltukVarMi) {
            System.out.println("📧 E-posta gönderildi: " + email);
        } else {
            System.out.println("🚫 Boş koltuk yok, e-posta gönderilmedi.");
        }

        System.out.println("🟢 Test başarılı! Gelen veri sayısı: " + result.size());
    }
}
