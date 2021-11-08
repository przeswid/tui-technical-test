package com.tui.proof.domain.impl.order;

import com.tui.proof.dto.OrderSearchingCriteria;
import com.tui.proof.model.Client;
import com.tui.proof.model.Client_;
import com.tui.proof.model.Order;
import com.tui.proof.model.Order_;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {
    private final EntityManager em;

    @Autowired
    public CustomOrderRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Order> searchOrders(OrderSearchingCriteria criteria) {
        EntityGraph<?> entityGraph = em.getEntityGraph("order_with_client_and_address_graph");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> fromOrder = cq.from(Order.class);
        Join<Order, Client> clientJoin = fromOrder.join(Order_.client);

        cq.select(fromOrder);
        cq.where(createPredicates(criteria, clientJoin, cb));
        TypedQuery<Order> query = em.createQuery(cq);
        query.setHint("javax.persistence.loadgraph", entityGraph);

        return query.getResultList();
    }

    private Predicate[] createPredicates(OrderSearchingCriteria criteria, Join<Order, Client> clientJoin, CriteriaBuilder cb) {
        return Stream.of(
                        predicate(cb, clientJoin.get(Client_.email), criteria.getClientEmail()),
                        predicate(cb, clientJoin.get(Client_.firstName), criteria.getClientFirstName()),
                        predicate(cb, clientJoin.get(Client_.lastName), criteria.getClientLastName()))
                .flatMap(Optional::stream)
                .toArray(Predicate[]::new);
    }

    private Optional<Predicate> predicate(CriteriaBuilder cb,
                                          Path<String> pathToArgument,
                                          String argumentValue) {
        if (argumentValue == null) {
            return Optional.empty();
        }

        // To consider: Because of "%" at the beginning DB INDEX will not be used!
        return Optional.of(cb.like(cb.upper(pathToArgument),
                "%" + argumentValue.toUpperCase() + "%"));
    }
}
