package com.example.JMSDemo.jms;

import com.example.JMSDemo.Utility.ObjectMapperUtility;
import com.example.JMSDemo.model.GiftCard;
import com.example.JMSDemo.service.GiftCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.example.JMSDemo.constants.Constants.*;

@Slf4j
@Component
public class JmsConsumer {

    @Autowired
    private GiftCardService giftCardService;
    @Autowired
    private ObjectMapperUtility objectMapperUtility;

    @JmsListener(destination = "giftcard.queue")
    public void receive(String giftCardAsString){
        String methodName = "receive()" + DELIMITER;
        GiftCard giftCard = objectMapperUtility.convertStringToGiftCardObject(giftCardAsString);

        log.info("{}Consumed {}{} from JMS giftcard.queue", methodName, GIFTCARD_OBJECT_AS_STRING, giftCardAsString);

        giftCardService.releaseGiftCard(giftCard.getGiftCardId());
        log.info("{}Released reservation for {}{}", methodName, GIFTCARD_OBJECT_AS_STRING, giftCardAsString);

        log.info("{}Exiting method which acknowledges consumption of the message from JMS. Message will be removed from queue.{}giftCardId={}", methodName, DELIMITER, giftCard.getGiftCardId());
    }
}