package com.example.JMSDemo.controller;

import com.example.JMSDemo.jms.JmsProducer;
import com.example.JMSDemo.model.GiftCard;
import com.example.JMSDemo.service.GiftCardService;
import static com.example.JMSDemo.constants.Constants.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/giftcards")
public class GiftCardController {

    private final GiftCardService giftCardService;
    private final JmsProducer jmsProducer;

    @Autowired
    public GiftCardController(GiftCardService giftCardService, JmsProducer jmsProducer){
        this.giftCardService = giftCardService;
        this.jmsProducer = jmsProducer;
    }

    @PostMapping("/reserve/{id}")
    public ResponseEntity<String> reserveGiftCard(@PathVariable String id){
        String methodName = "reserveGiftCard()" + DELIMITER;
        log.debug("{}{}", methodName, ENTER);

        log.info("{}Received request with giftCardId={}", methodName, id);

        log.debug("{}Checking if gift card with giftCardId={} is already reserved.", methodName, id);

        if(giftCardService.isGiftCardReserved(id)){

            log.debug("{}Gift card with giftCardId={} is already reserved. Will NOT send gift card to JMS.", methodName, id);
            return ResponseEntity.badRequest().body("Gift Card is already reserved.");
        }

        log.debug("{}Gift card with giftCardId={} is NOT already reserved. Reserving gift card...", methodName, id);
        String giftCardAsString = giftCardService.reserveGiftCard(id);

        log.info("{}Sending gift card with id={} to JMS...", methodName, id);
        jmsProducer.send(giftCardAsString);

        return ResponseEntity.ok("Gift Card reserved");
    }
}
