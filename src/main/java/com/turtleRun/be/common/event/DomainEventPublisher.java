package com.turtleRun.be.common.event;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
