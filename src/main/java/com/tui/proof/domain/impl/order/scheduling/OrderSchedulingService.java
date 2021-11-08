package com.tui.proof.domain.impl.order.scheduling;


import com.tui.proof.domain.api.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
class OrderSchedulingService {

    private final OrderService orderService;

    @Autowired
    public OrderSchedulingService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 10000)
    public void setOrdersAsPreparedToCooking() {
        log.debug("Order Scheduling Cron - start");
        orderService.sendOrdersToProcessing();
        log.debug("Order Scheduling Cron - end");
    }
}
