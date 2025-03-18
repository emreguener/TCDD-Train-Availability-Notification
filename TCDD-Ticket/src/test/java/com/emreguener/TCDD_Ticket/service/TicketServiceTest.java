package com.emreguener.TCDD_Ticket.service;

import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import com.emreguener.TCDD_Ticket.dto.SeferDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Test
    void testGetTrainWithPagination() {
        System.out.println("🟢 Test başladı...");

        // Swagger UI formatına uygun tarih stringi
        String gidisTarihStr = "2025-03-28T10:00:00";
        String gidisTarihSonStr = "2025-03-28T23:59:59";

        // Tarihleri Swagger formatından LocalDateTime'a çevir
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime gidisTarih = LocalDateTime.parse(gidisTarihStr, formatter);
        LocalDateTime gidisTarihSon = LocalDateTime.parse(gidisTarihSonStr, formatter);

        // Swagger UI'de kullanılan istasyon ve ID'ler
        String binisIstasyonu = "ESKİŞEHİR";
        String inisIstasyonu = "BALIKESİR";
        int binisIstasyonId = 93;
        int inisIstasyonId = 1159;
        String koltukTipi = "EKONOMİ";
        String email = "tcdddenemejava@gmail.com";  // Test için e-posta adresi

        // Sayfalama için parametreler
        int page = 0;
        int size = 5;

        // Servis metodunu çağır ve `Page<SeferDTO>` sonucu al
        Page<SeferDTO> trainResults = ticketService.getTrain(
                gidisTarih, gidisTarihSon, binisIstasyonu, inisIstasyonu, binisIstasyonId, inisIstasyonId, koltukTipi, email, page, size);

        System.out.println("✅ Servis çağrıldı, gelen sayfa: " + trainResults.getNumber() + ", toplam sayfa: " + trainResults.getTotalPages());
        System.out.println("📌 Gelen veri sayısı (sayfalı): " + trainResults.getNumberOfElements());

        // Gelen tren verilerini detaylı yazdıralım
        System.out.println("📌 Gelen Trenler:");
        trainResults.forEach(train -> {
            System.out.println("🚆 Tren Adı: " + train.getTrenAdi());
            System.out.println("📅 Kalkış Tarihi: " + train.getKalkisTarihi());
            System.out.println("🚉 Biniş İstasyonu: " + train.getBinisIstasyonu());
            System.out.println("🏁 Varış İstasyonu: " + train.getVarisIstasyonu());
            System.out.println("💺 Koltuk Tipi: " + train.getKoltukTipi());
            System.out.println("🎟️ Boş Koltuk Sayısı: " + train.getBosKoltukSayisi());
            System.out.println("-------------------------------------");
        });

        // Sonuçları doğrula
        assertNotNull(trainResults, "❌ Dönen sonuç null olmamalı!");
        assertFalse(trainResults.isEmpty(), "❌ Liste boş olmamalı!");
        assertTrue(trainResults.getTotalElements() > 0, "❌ En az 1 tren olmalı!");
        assertTrue(trainResults.getNumberOfElements() <= size, "❌ Dönen eleman sayısı sayfa boyutundan büyük olmamalı!");

        // Eğer boş koltuk varsa e-posta gönderiminin yapılmış olmasını bekliyoruz
        boolean bosKoltukVarMi = trainResults.getContent().stream().anyMatch(train -> train.getBosKoltukSayisi() > 0);
        if (bosKoltukVarMi) {
            System.out.println("📧 E-posta gönderildi: " + email);
        } else {
            System.out.println("🚫 Boş koltuk yok, e-posta gönderilmedi.");
        }

        System.out.println("🟢 Test başarılı! Gelen sayfadaki veri sayısı: " + trainResults.getNumberOfElements());
    }
}
