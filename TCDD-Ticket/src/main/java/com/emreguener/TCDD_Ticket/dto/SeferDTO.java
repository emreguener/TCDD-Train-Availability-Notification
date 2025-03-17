package com.emreguener.TCDD_Ticket.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Getter, Setter, toString ve equals/hashCode ekler
@AllArgsConstructor
@NoArgsConstructor  // Jackson'ın serileştirme için boş constructor istemesi nedeniyle
public class SeferDTO {

    @Schema(description = "Tren Adı", example = "ANKARA EKSPRESİ")
    private String trenAdi;

    @Schema(description = "Kalkış Tarihi", example = "2025-03-24T19:00:00")
    private String kalkisTarihi;

    @Schema(description = "Biniş İstasyonu", example = "Eskişehir")
    private String binisIstasyonu;

    @Schema(description = "Varış İstasyonu", example = "İstanbul (Söğütlüçeşme)")
    private String varisIstasyonu;

    @Schema(description = "Koltuk Tipi", example = "EKONOMİ")
    private String koltukTipi;

    @Schema(description = "Boş Koltuk Sayısı", example = "262")
    private int bosKoltukSayisi;
}
