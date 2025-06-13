package com.turtleRun.be.common.model;

import com.turtleRun.be.common.event.DomainEvent;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
@Getter
public abstract class BaseAggregateRoot extends BaseEntity {
    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}