package io.hotely.hotel.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.hotely.hotel.utils.Timestamper;

@Service
public class Consumer {

  private final Logger logger = LoggerFactory.getLogger(Consumer.class);

  @Autowired
  private Timestamper timestamper;

  // Listens to customer service
  @KafkaListener(topics = "customer-service", groupId = "group_id")
  public void consume(String message) {
    logger.info(String.format("#### Log time: %s -> Consumed message: -> %s", timestamper.timestamp(), message));
  }
}
