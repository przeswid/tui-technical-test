package com.tui.proof.domain.impl.order;

import com.tui.proof.dto.OrderSearchingCriteria;
import com.tui.proof.model.Order;

import java.util.List;

interface CustomOrderRepository {
    List<Order> searchOrders(OrderSearchingCriteria criteria);
}
