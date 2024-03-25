package com.dholubeu.rideservice.kafka;

public interface KfProducer {

    void send(Message message);

}