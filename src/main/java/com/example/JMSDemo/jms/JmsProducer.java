package com.example.JMSDemo.jms;

import com.example.JMSDemo.Utility.ObjectMapperUtility;
import com.example.JMSDemo.model.GiftCard;
import com.example.JMSDemo.service.GiftCardService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import static com.example.JMSDemo.constants.Constants.*;

@Slf4j
@Service
public class JmsProducer {

    private JmsTemplate jmsTemplate;
    private Queue queue;
    private ObjectMapperUtility objectMapperUtility;

    @Autowired
    public JmsProducer(JmsTemplate jmsTemplate, Queue queue, ObjectMapperUtility objectMapperUtility){
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
        this.objectMapperUtility = objectMapperUtility;
    }

    public void send(GiftCard giftCard){
        String methodName = "send()" + DELIMITER;
        log.info("{}{}", methodName, ENTER);

        String giftCardObjectAsString = objectMapperUtility.getGiftCardObjectAsString(giftCard);

        jmsTemplate.send(queue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage(giftCardObjectAsString);
                // Set delay for 10 minutes (600,000 milliseconds)
                message.setLongProperty("AMQ_SCHEDULED_DELAY", 30000);
                return message;
            }
        });

        log.info("{}Sent {}{}{}{}", methodName, GIFTCARD_OBJECT_AS_STRING, giftCardObjectAsString, DELIMITER, EXIT);
    }
}
