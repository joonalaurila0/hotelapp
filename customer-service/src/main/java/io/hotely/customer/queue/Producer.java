package io.hotely.customer.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.hotely.customer.utils.Timestamper;

@Service
public class Producer {

  private final Logger logger = LoggerFactory.getLogger(Producer.class);

  @Autowired
  private Timestamper timestamper;

  private static final String TOPIC = "customer-service";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  /* If you’re going to pass state in your message, make sure to include
   * a date-time stamp or version number so that the service that’s
   * consuming the data can inspect the data passed to it and ensure
   * that it’s not older than the copy of the data it already has.
  */
  public void sendMessage(String message) {
    logger.info(String.format("#### Log time: %s -> Producing message: -> %s", timestamper.timestamp(), message));
    this.kafkaTemplate.send(TOPIC, message);
  }
}
