package com.tui.proof.domain.api;

import com.tui.proof.dto.OrderDto;
import com.tui.proof.dto.OrderSearchingCriteria;
import com.tui.proof.dto.RegisterOrderDto;
import com.tui.proof.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(RegisterOrderDto orderDto);

    Optional<Order> getOrder(String number);

    Order updateOrder(OrderDto orderDto);

    List<Order> searchOrders(OrderSearchingCriteria criteria);

    void sendOrdersToProcessing();
}
