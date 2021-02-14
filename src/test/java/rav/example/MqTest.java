package rav.example;

import org.junit.jupiter.api.Test;

import javax.jms.*;
import java.util.Enumeration;

public class MqTest extends TestBase {

    @Test
    void produceAndConsumeMessages() {
        Queue queue = context.createQueue("queue:///" + properties.getMqQueueName());
        JMSProducer producer = context.createProducer();
        QueueBrowser browser = context.createBrowser(queue);
        JMSConsumer consumer = context.createConsumer(queue);
        TextMessage message1 = context.createTextMessage("Message1");
        TextMessage message2 = context.createTextMessage("Message2");

        producer.send(queue, message1);
        producer.send(queue, message2);
        logger.info("===== SEND 2 messages =====");

        try {
            Enumeration<?> messages = browser.getEnumeration();
            logger.info("===== BROWSE messages =====");
            while (messages.hasMoreElements()) {
                logger.info(messages.nextElement().toString());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        logger.info("===== CONSUME message =====");
//        logger.info(String.valueOf(consumer.receive()));
        logger.info(consumer.receiveBody(String.class, 10_000));
    }
}
