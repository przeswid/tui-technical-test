package com.tui.proof.domain.impl;

import com.tui.proof.domain.api.OrderService;
import com.tui.proof.dto.RegisterOrderDto;
import com.tui.proof.model.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order createOrder(RegisterOrderDto orderDto) {
        return null;
    }

    @Override
    public Optional<Order> getOrder(String number) {
        return Optional.empty();
    }
}
