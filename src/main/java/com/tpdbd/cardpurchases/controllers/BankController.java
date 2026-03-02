package com.tpdbd.cardpurchases.controllers;

import com.tpdbd.cardpurchases.model.Bank;
import com.tpdbd.cardpurchases.services.BankService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/banks")
public class BankController {

    private final BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }
    /**
     * POST /api/banks
     * Crear un nuevo banco
     */
    @PostMapping
    public ResponseEntity<Bank> createBank(
            @RequestParam String name,
            @RequestParam String cuit,
            @RequestParam String address,
            @RequestParam String telephone,
            @RequestParam String direction) {

        Bank bank = bankService.createBank(name, cuit, address, telephone, direction);
        return ResponseEntity.status(HttpStatus.CREATED).body(bank);
    }

    /**
     * GET /api/banks/most-purchases
     * Obtener banco con mayor cantidad de compras
     */
    @GetMapping("/most-purchases")
    public ResponseEntity<Bank> getBankWithMostPurchases() {
        Bank bank = bankService.getBankWithMostPurchases();
        return ResponseEntity.ok(bank);
    }

    /**
     * GET /api/banks/client-count
     * Obtener número de clientes de cada banco
     */
    @GetMapping("/client-count")
    public ResponseEntity<Map<String, Long>> getClientCountByBank() {
        Map<String, Long> clientCount = bankService.getClientCountByBank();
        return ResponseEntity.ok(clientCount);
    }

}
