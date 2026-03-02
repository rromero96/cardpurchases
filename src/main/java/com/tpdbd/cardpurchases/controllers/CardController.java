package com.tpdbd.cardpurchases.controllers;

import com.tpdbd.cardpurchases.model.Card;
import com.tpdbd.cardpurchases.services.CardService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    /**
     * POST /api/cards
     * Crear una nueva tarjeta
     */
    @PostMapping
    public ResponseEntity<Card> createCard(
            @RequestParam Long cardHolderId,
            @RequestParam Long bankId,
            @RequestParam String number,
            @RequestParam String ccv,
            @RequestParam String cardholderNameInCard,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate) {

        Card card = cardService.createCard(cardHolderId, bankId, number, ccv, cardholderNameInCard, since, expirationDate);
        return ResponseEntity.status(HttpStatus.CREATED).body(card);
    }

    /**
     * GET /api/cards/old
     * Obtener tarjetas emitidas hace más de 5 años
     */
    @GetMapping("/old")
    public ResponseEntity<List<String>> getCardsIssuedMoreThan5YearsAgo() {
        List<Card> cards = cardService.getCardsIssuedMoreThan5YearsAgo();
        List<String> cardNumbers = cards.stream()
                .map(Card::getNumber)
                .toList();
        return ResponseEntity.ok(cardNumbers);
    }

}
