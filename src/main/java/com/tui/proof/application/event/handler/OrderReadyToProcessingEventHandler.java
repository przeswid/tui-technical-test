package com.tui.proof.application.event.handler;

import com.tui.proof.common.events.OrderReadyToProcessingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderReadyToProcessingEventHandler implements ApplicationListener<OrderReadyToProcessingEvent> {
    @Override
    public void onApplicationEvent(OrderReadyToProcessingEvent event) {
        log.info("Please send this message to Miguel's notification channel: [pilotes quantity: {}, order number: {}",
                event.getPilotes(), event.getOrderNumber());
    }
}
