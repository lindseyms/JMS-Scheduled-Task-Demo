package com.example.JMSDemo.service;

import com.example.JMSDemo.Utility.ObjectMapperUtility;
import com.example.JMSDemo.model.GiftCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.JMSDemo.constants.Constants.*;

@Slf4j
@Service
public class GiftCardService {
    private final Map<String, GiftCard> giftCardMap;

    private final ObjectMapperUtility objectMapperUtility;

    @Autowired
    public GiftCardService(ObjectMapperUtility objectMapperUtility){
        this.objectMapperUtility = objectMapperUtility;
        this.giftCardMap = new ConcurrentHashMap<>();
    }

    //Method to reserve a gift card
    public GiftCard reserveGiftCard(String id){
        String methodName = "reserveGiftCard()" + DELIMITER;
        log.info("{}{}", methodName, ENTER);

        GiftCard giftCard = new GiftCard(id, true, Instant.now());

        String giftCardAsString = objectMapperUtility.getGiftCardObjectAsString(giftCard);

        giftCardMap.put(id, giftCard);
        log.info("{}Added GiftCard object to giftCardMap{}{}{}", methodName, DELIMITER, GIFTCARD_OBJECT_AS_STRING, giftCardAsString);

        log.info("{}{}{}{}", methodName, EXIT_WITH, GIFTCARD_OBJECT_AS_STRING, giftCardAsString);
        return giftCard;
    }

    //Method to release/unreserve a gift card
    public void releaseGiftCard(String giftCardId){
        String methodName = "releaseGiftCard()" + DELIMITER;
        log.info("{}{}", methodName + ENTER);

        GiftCard giftCard = giftCardMap.get(giftCardId);
        if(giftCard != null){
            log.info("{}Setting isReserved to false for gift card with ID={}", methodName, giftCardId);
            giftCard.setReserved(false);

            int counter = 1;
            log.info("{}All GiftCards in giftCardMap:", methodName);
            for(String id : giftCardMap.keySet()){
                System.out.println(counter + ". " + objectMapperUtility.getGiftCardObjectAsString(giftCardMap.get(id)));
                counter++;
            }
        }
        else{
            log.info("{}Gift card does not exist in the database. Could not set isReserved=false.", methodName);
        }

        log.info("{}{}", methodName, EXIT);
    }

    //Method to check if a gift card is reserved
    public boolean isGiftCardReserved(String giftCardId){
        String methodName = "isGiftCardReserved()" + DELIMITER;
        log.info("{}{}", methodName, ENTER);

        GiftCard giftCard = giftCardMap.get(giftCardId);

        boolean isGiftCardReserved = giftCard != null && giftCard.isReserved();

        log.info("{}{}isGiftCardReserved={}", methodName, EXIT_WITH, isGiftCardReserved);

        return isGiftCardReserved;
    }
}