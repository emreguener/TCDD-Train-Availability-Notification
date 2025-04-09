package com.emreguener.TCDD_Ticket.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.emreguener.TCDD_Ticket.dto.RequestDTO;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import com.emreguener.TCDD_Ticket.dto.SeferDTO;

@Service
public class TicketService {

    private final RestClient restClient;
    private final EmailService emailService;

    private static final String API_URL = "https://web-api-prod-ytp.tcddtasimacilik.gov.tr/tms/train/train-availability?environment=dev&userId=1";
    private static final String BEARER_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlVFFicDhDMmpiakp1cnUzQVk2a0ZnV196U29MQXZIMmJ5bTJ2OUg5THhRIn0.eyJleHAiOjE3MjEzODQ0NzAsImlhdCI6MTcyMTM4NDQxMCwianRpIjoiYWFlNjVkNzgtNmRkZS00ZGY4LWEwZWYtYjRkNzZiYjZlODNjIiwiaXNzIjoiaHR0cDovL3l0cC1wcm9kLW1hc3RlcjEudGNkZHRhc2ltYWNpbGlrLmdvdi50cjo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMDAzNDI3MmMtNTc2Yi00OTBlLWJhOTgtNTFkMzc1NWNhYjA3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG1zIiwic2Vzc2lvbl9zdGF0ZSI6IjAwYzM4NTJiLTg1YjEtNDMxNS04OGIwLWQ0MWMxMTcyYzA0MSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjAwYzM4NTJiLTg1YjEtNDMxNS04OGIwLWQ0MWMxMTcyYzA0MSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoid2ViIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AIW_4Qws2wfwxyVg8dgHRT9jB3qNavob2C4mEQIQGl3urzW2jALPx-e51ZwHUb-TXB-X2RPHakonxKnWG6tDIP5aKhiidzXDcr6pDDoYU5DnQhMg1kywyOaMXsjLFjuYN5PAyGUMh6YSOVsg1PzNh-5GrJF44pS47JnB9zk03Pr08napjsZPoRB-5N4GQ49cnx7ePC82Y7YIc-gTew2baqKQPz9_v381Gbm2V38PZDH9KldlcWut7kqQYJFMJ7dkM_entPJn9lFk7R5h5j_06OlQEpWRMQTn9SQ1AYxxmZxBu5XYMKDkn4rzIIVCkdTPJNCt5PvjENjClKFeUA1DOg";

    public TicketService(RestClient restClient, EmailService emailService) {
        this.restClient = restClient;
        this.emailService = emailService;
    }

    public Page<SeferDTO> getTrain(LocalDateTime gidisTarih, LocalDateTime gidisTarihSon,
                                    String binisIstasyonu, String inisIstasyonu,
                                    int binisIstasyonId, int inisIstasyonId,
                                    String koltukTipi, String email, int page, int size) {

        List<Train> filteredTrains = collectTrainsForDateRange(
                binisIstasyonId, binisIstasyonu,
                inisIstasyonId, inisIstasyonu,
                gidisTarih, gidisTarihSon,
                koltukTipi
        );

        List<SeferDTO> seferListesi = filteredTrains.stream().map(train -> {
            // Kullanıcının seçtiği koltuk tipi için boş koltuk sayısını bul
            int bosKoltukSayisi = train.availableFareInfo().stream()
                    .flatMap(fare -> fare.cabinClasses().stream())
                    .filter(cabin -> cabin.cabinClass().name().equalsIgnoreCase(koltukTipi))
                    .mapToInt(cabin -> cabin.availabilityCount())
                    .findFirst()
                    .orElse(0);

            return new SeferDTO(
                    train.name(),
                    train.trainSegments().get(0).departureTime(),
                    binisIstasyonu,
                    inisIstasyonu,
                    koltukTipi,
                    bosKoltukSayisi
            );
        }).collect(Collectors.toList());

        if (email != null && !email.isEmpty() && !seferListesi.isEmpty()) {
            String emailContent = generateEmailContent(seferListesi);
            emailService.sendEmail(email, "Tren Seferleri Bilgisi", emailContent);
        }

        Pageable pageable = PageRequest.of(page, size);
        int start = Math.min((int) pageable.getOffset(), seferListesi.size());
        int end = Math.min((start + pageable.getPageSize()), seferListesi.size());
        List<SeferDTO> pagedList = seferListesi.subList(start, end);

        return new PageImpl<>(pagedList, pageable, seferListesi.size());
    }

    private List<Train> collectTrainsForDateRange(
            int binisIstasyonId, String binisIstasyonu,
            int inisIstasyonId, String inisIstasyonu,
            LocalDateTime startDate, LocalDateTime endDate,
            String koltukTipi) {

        List<Train> allTrains = new ArrayList<>();
        LocalDateTime currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            RequestDTO requestDTO = new RequestDTO(
                    "DOMESTIC",
                    false,
                    List.of(new RequestDTO.PassengerTypeCount(0, 1)),
                    List.of(new RequestDTO.SearchRoutes(
                            binisIstasyonId, binisIstasyonu,
                            inisIstasyonId, inisIstasyonu,
                            currentDate
                    ))
            );

            ResponseDTO response = restClient.post()
                    .uri(API_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .header("Unit-Id", "3895")
                    .body(requestDTO)
                    .retrieve()
                    .body(ResponseDTO.class);

            if (response != null && response.trainLegs() != null) {
                for (var leg : response.trainLegs()) {
                    for (var availability : leg.trainAvailabilities()) {
                        for (var train : availability.trains()) {
                            boolean hasMatchingCabin = train.availableFareInfo().stream()
                                    .flatMap(fare -> fare.cabinClasses().stream())
                                    .anyMatch(cabin -> cabin.cabinClass().name().equalsIgnoreCase(koltukTipi)
                                            && cabin.availabilityCount() > 0);
                            if (hasMatchingCabin) {
                                allTrains.add(train);
                            }
                        }
                    }
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return allTrains;
    }

    private String generateEmailContent(List<SeferDTO> seferListesi) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tren Seferleri:\n\n");
        for (SeferDTO sefer : seferListesi) {
            sb.append("Tren Adı: ").append(sefer.getTrenAdi()).append("\n")
              .append("Kalkış Tarihi: ").append(sefer.getKalkisTarihi()).append("\n")
              .append("Biniş İstasyonu: ").append(sefer.getBinisIstasyonu()).append("\n")
              .append("Varış İstasyonu: ").append(sefer.getVarisIstasyonu()).append("\n")
              .append("Koltuk Tipi: ").append(sefer.getKoltukTipi()).append("\n")
              .append("✅ Boş Koltuk Sayısı: ").append(sefer.getBosKoltukSayisi()).append("\n\n");
        }
        return sb.toString();
    }
} 
