package com.tpdbd.cardpurchases.controllers;

import com.tpdbd.cardpurchases.model.CardHolder;
import com.tpdbd.cardpurchases.services.CardHolderService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cardholders")
public class CardHolderController {

    private final CardHolderService cardHolderService;

    @Autowired
    public CardHolderController(CardHolderService cardHolderService) {
        this.cardHolderService = cardHolderService;
    }
    /**
     * POST /api/cardholders
     * Crear un nuevo titular de tarjeta
     */
    @PostMapping
    public ResponseEntity<CardHolder> createCardHolder(
            @RequestParam String completeName,
            @RequestParam String dni,
            @RequestParam String cuil,
            @RequestParam String address,
            @RequestParam String telephone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate) {

        CardHolder cardHolder = cardHolderService.createCardHolder(
                completeName, dni, cuil, address, telephone, entryDate
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(cardHolder);
    }

    /**
     * GET /api/cardholders/top-10-spending
     * Obtener top 10 titulares con mayor monto total en compras
     */
    @GetMapping("/top-10-spending")
    public ResponseEntity<List<CardHolder>> getTop10CardholdersBySpending() {
        List<CardHolder> cardholders = cardHolderService.getTop10CardholdersBySpending();
        return ResponseEntity.ok(cardholders);
    }

}
