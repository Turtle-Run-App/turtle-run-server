package com.turtleRun.be.common.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    LocalDateTime getOccurredAt();
}
