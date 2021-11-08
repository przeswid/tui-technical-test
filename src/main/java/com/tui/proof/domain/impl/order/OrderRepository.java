package com.tui.proof.domain.impl.order;

import com.tui.proof.common.OrderState;
import com.tui.proof.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long>, CustomOrderRepository {

    @EntityGraph(value = "order_with_client_and_address_graph")
    Optional<Order> findByNumber(String number);

    @Query("SELECT o FROM Order o WHERE o.number = ?1")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Order> findByNumberForUpdate(String number);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Order> findByStateAndCreatedOnLessThan(OrderState state, LocalDateTime createdOn);
}
