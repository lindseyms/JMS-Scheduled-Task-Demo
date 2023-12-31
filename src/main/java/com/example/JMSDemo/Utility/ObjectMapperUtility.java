package com.example.JMSDemo.Utility;

import com.example.JMSDemo.model.GiftCard;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.JMSDemo.constants.Constants.*;

@Slf4j
@Component
public class ObjectMapperUtility {

    @Autowired
    ObjectMapper objectMapper;

    public String getGiftCardObjectAsString(GiftCard giftCard){
        String methodName = "getGiftCardObjectAsString()" + DELIMITER;

        String giftCardAsString = null;
        try {
            giftCardAsString = objectMapper.writeValueAsString(giftCard);
            log.debug("{}{}{}", methodName, GIFTCARD_OBJECT_AS_STRING, giftCardAsString);
        } catch (JsonProcessingException e) {
            log.info("{}JsonProcessingException was thrown while mapping GiftCard object to String.{}{}{}", methodName, DELIMITER, ERROR_DETAIL, e.getMessage());
        }

        return giftCardAsString;
    }

    public GiftCard convertStringToGiftCardObject(String giftCardAsString){
        String methodName = "convertStringToGiftCardObject()" + DELIMITER;

        GiftCard giftCard = null;
        try {
            giftCard = objectMapper.readValue(giftCardAsString, GiftCard.class);
            log.debug("{}Converted GiftCard with ID={} from String to Object.", methodName, giftCard.getGiftCardId());
        } catch (JsonProcessingException e) {
            log.info("{}JsonProcessingException was thrown while mapping String to GiftCard Object.{}{}{}", methodName, DELIMITER, ERROR_DETAIL, e.getMessage());
        }

        return giftCard;

    }
}
