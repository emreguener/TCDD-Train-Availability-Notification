package com.emreguener.TCDD_Ticket.service;

import com.emreguener.TCDD_Ticket.dto.RequestDTO;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO.Train;
import com.emreguener.TCDD_Ticket.dto.ResponseDTO.TrainSegment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TicketService {

    private final RestClient restClient;
    private static final String API_URL = "https://web-api-prod-ytp.tcddtasimacilik.gov.tr/tms/train/train-availability?environment=dev&userId=1";
    private static final String BEARER_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJlVFFicDhDMmpiakp1cnUzQVk2a0ZnV196U29MQXZIMmJ5bTJ2OUg5THhRIn0.eyJleHAiOjE3MjEzODQ0NzAsImlhdCI6MTcyMTM4NDQxMCwianRpIjoiYWFlNjVkNzgtNmRkZS00ZGY4LWEwZWYtYjRkNzZiYjZlODNjIiwiaXNzIjoiaHR0cDovL3l0cC1wcm9kLW1hc3RlcjEudGNkZHRhc2ltYWNpbGlrLmdvdi50cjo4MDgwL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMDAzNDI3MmMtNTc2Yi00OTBlLWJhOTgtNTFkMzc1NWNhYjA3IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoidG1zIiwic2Vzc2lvbl9zdGF0ZSI6IjAwYzM4NTJiLTg1YjEtNDMxNS04OGIwLWQ0MWMxMTcyYzA0MSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1tYXN0ZXIiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJvcGVuaWQgZW1haWwgcHJvZmlsZSIsInNpZCI6IjAwYzM4NTJiLTg1YjEtNDMxNS04OGIwLWQ0MWMxMTcyYzA0MSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicHJlZmVycmVkX3VzZXJuYW1lIjoid2ViIiwiZ2l2ZW5fbmFtZSI6IiIsImZhbWlseV9uYW1lIjoiIn0.AIW_4Qws2wfwxyVg8dgHRT9jB3qNavob2C4mEQIQGl3urzW2jALPx-e51ZwHUb-TXB-X2RPHakonxKnWG6tDIP5aKhiidzXDcr6pDDoYU5DnQhMg1kywyOaMXsjLFjuYN5PAyGUMh6YSOVsg1PzNh-5GrJF44pS47JnB9zk03Pr08napjsZPoRB-5N4GQ49cnx7ePC82Y7YIc-gTew2baqKQPz9_v381Gbm2V38PZDH9KldlcWut7kqQYJFMJ7dkM_entPJn9lFk7R5h5j_06OlQEpWRMQTn9SQ1AYxxmZxBu5XYMKDkn4rzIIVCkdTPJNCt5PvjENjClKFeUA1DOg"; 

    public TicketService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Set<Train> getTrain(LocalDateTime gidisTarih, LocalDateTime gidisTarihSon,
                               String binisIstasyonu, String inisIstasyonu,
                               int binisIstasyonId, int inisIstasyonId,
                               String koltukTipi) {

        // API'ye istek için RequestDTO nesnesi oluştur
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setSearchRoutes(List.of(new RequestDTO.SearchRoutes(
                binisIstasyonId, binisIstasyonu,
                inisIstasyonId, inisIstasyonu,
                gidisTarih
        )));
        requestDTO.setSearchType("DOMESTIC");
        requestDTO.setSearchReservation(false);
        requestDTO.setPassengerTypeCounts(List.of(new RequestDTO.PassengerTypeCount(0, 1)));

        // API'den gelen yanıt
        ResponseDTO response = restClient.post()
                .uri(API_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + BEARER_TOKEN)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header("Unit-Id", "3895")
                .header("Accept-Language", "tr")
                .header("Referer", "https://ebilet.tcddtasimacilik.gov.tr")
                .header(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36")
                .body(requestDTO)
                .retrieve()
                .body(ResponseDTO.class);

        // Trenleri filtrelemek için Set kullanıyoruz (Tekrarlamayı engellemek için)
        Set<Train> filteredTrains = new HashSet<>();

        // Saat formatı
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Trenleri filtrele
        response.getTrainLegs().forEach(trainLeg ->
                trainLeg.getTrainAvailabilities().forEach(trainAvailability ->
                        trainAvailability.getTrains().forEach(train -> {
                            for (TrainSegment segment : train.getTrainSegments()) {
                                LocalDateTime departureTime = LocalDateTime.parse(segment.getDepartureTime(), formatter);
                                if (departureTime.isAfter(gidisTarih) && departureTime.isBefore(gidisTarihSon)) {
                                    train.getAvailableFareInfo().forEach(fareInfo ->
                                            fareInfo.getCabinClasses().forEach(cabinClass -> {
                                                if (cabinClass.getCabinClass().getName().equalsIgnoreCase(koltukTipi) &&
                                                        cabinClass.getAvailabilityCount() > 0) {
                                                    filteredTrains.add(train);
                                                }
                                            })
                                    );
                                }
                            }
                        })
                )
        );

        return filteredTrains;
    }
}
