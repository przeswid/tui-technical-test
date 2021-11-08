package com.tui.proof.domain.impl.order;

import com.tui.proof.common.OrderState;
import com.tui.proof.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = "order_with_client_and_address_graph")
    Optional<Order> findByNumber(String number);
}
