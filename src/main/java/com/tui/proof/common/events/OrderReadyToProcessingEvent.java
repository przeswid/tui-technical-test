package com.tui.proof.common.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderReadyToProcessingEvent extends ApplicationEvent {
    private final String orderNumber;

    private final Integer pilotes;

    public OrderReadyToProcessingEvent(Object source, String orderNumber, Integer pilotes) {
        super(source);
        this.orderNumber = orderNumber;
        this.pilotes = pilotes;
    }
}
