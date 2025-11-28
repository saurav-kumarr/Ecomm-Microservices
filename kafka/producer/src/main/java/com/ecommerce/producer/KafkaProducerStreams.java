package com.ecommerce.producer;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.function.Supplier;

@Configuration
public class KafkaProducerStreams {

    @Bean
    public Supplier<RiderLocation> sendRiderLocation() {
        Random random = new Random();
        return () -> {
            String riderId = "rider" + random.nextInt(20);
            RiderLocation location = new RiderLocation(
                    riderId, 789.00,123.00);
            System.out.println("Sending Rider location: " + location.getRiderId());
            return location;
        };
    }

}
