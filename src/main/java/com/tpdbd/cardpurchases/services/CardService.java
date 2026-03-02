package com.tpdbd.cardpurchases.services;

import com.tpdbd.cardpurchases.model.Card;
import com.tpdbd.cardpurchases.model.CardHolder;
import com.tpdbd.cardpurchases.model.Bank;
import com.tpdbd.cardpurchases.repositories.CardRepository;
import com.tpdbd.cardpurchases.repositories.CardHolderRepository;
import com.tpdbd.cardpurchases.repositories.BankRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CardHolderRepository cardHolderRepository;
    private final BankRepository bankRepository;


    @Autowired
    public CardService(CardRepository cardRepository, CardHolderRepository cardHolderRepository, BankRepository bankRepository) {
        this.cardRepository = cardRepository;
        this.cardHolderRepository = cardHolderRepository;
        this.bankRepository = bankRepository;
    }
    /**
     * Crear una nueva tarjeta
     */
    @Transactional
    public Card createCard(Long cardHolderId, Long bankId, String number,
                           String cvc, String cardholderNameInCard, LocalDate since, LocalDate expirationDate) {

        CardHolder cardHolder = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new IllegalArgumentException("Titular no encontrado"));

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new IllegalArgumentException("Banco no encontrado"));

        Card card = new Card();
        card.setNumber(number);
        card.setCcv(cvc);
        card.setCardholderNameInCard(cardholderNameInCard);
        card.setSince(since);
        card.setExpirationDate(expirationDate);
        card.setCardholder(cardHolder);
        card.setBank(bank);

        return cardRepository.save(card);
    }

    /**
     * Obtener tarjetas emitidas hace más de 5 años
     */
    public List<Card> getCardsIssuedMoreThan5YearsAgo() {
        return cardRepository.findCardsIssuedMoreThan5YearsAgo();
    }

}
