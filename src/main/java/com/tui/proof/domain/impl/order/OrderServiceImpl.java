package com.tui.proof.domain.impl.order;

import com.tui.proof.common.OrderState;
import com.tui.proof.common.events.OrderReadyToProcessingEvent;
import com.tui.proof.common.exception.OrderModificationDeniedException;
import com.tui.proof.common.exception.OrderProcessingException;
import com.tui.proof.common.exception.ResourceNotFoundException;
import com.tui.proof.common.util.DateUtil;
import com.tui.proof.domain.api.AddressService;
import com.tui.proof.domain.api.ClientService;
import com.tui.proof.domain.api.OrderService;
import com.tui.proof.dto.*;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
class OrderServiceImpl implements OrderService {

    private final ClientService clientService;

    private final AddressService addressService;

    private final OrderRepository orderRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    private BigDecimal piloteUnitPrice;

    private Integer sendToProcessingAfterMinutes;

    public OrderServiceImpl(ClientService clientService, AddressService addressService,
                            OrderRepository orderRepository) {
        this.clientService = clientService;
        this.addressService = addressService;
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(RegisterOrderDto orderDto) {
        log.debug("Creating new Order entity: {}", orderDto);
        Client client = createClientIfDoesntExist(orderDto.getClient());
        Address address = createAddressIfDoesntExist(orderDto.getAddress());
        clientService.addAddressToClient(address.getId(), client.getId());

        return orderRepository.save(createNewOrder(orderDto, client, address));
    }

    @Override
    public Optional<Order> getOrder(String number) {
        log.debug("Getting order by number: {}", number);
        return orderRepository.findByNumber(number);
    }

    @Override
    public Order updateOrder(OrderDto orderDto) {
        log.debug("Updating order with data: {}", orderDto);
        Order order = orderRepository.findByNumberForUpdate(orderDto.getNumber())
                .orElseThrow(() -> new ResourceNotFoundException(orderDto.getNumber()));

        validateOrderOnUpdate(order, orderDto);

        Address address = createAddressIfDoesntExist(orderDto.getAddress());
        order.setAddress(address);
        order.setPilotes(orderDto.getPilotes());
        order.setOrderTotal(piloteUnitPrice.multiply(
                BigDecimal.valueOf(orderDto.getPilotes())));

        return orderRepository.save(order);
    }

    @Override
    public List<Order> searchOrders(OrderSearchingCriteria criteria) {
        return orderRepository.searchOrders(criteria);
    }

    /**
     *  Method that could be invoked in parallel.
     *  Method uses PESIMISTIC_LOCK to lock all the orders that will be sent
     *  to processing. The same type of lock is used in method updateOrder.
     *  This solution should prevent from race condition that can occur when the
     *  order X is updating and sending to processing in the same time.
     */
    public void sendOrdersToProcessing() {
        List<Order> orders = orderRepository.findByStateAndCreatedOnLessThan(
                OrderState.NEW, DateUtil.now().minusMinutes(sendToProcessingAfterMinutes));

        for (Order order : orders) {
            order.setState(OrderState.SENT_TO_PROCESSING);
            sendOrderProcessingEvent(order);
        }
    }

    @Autowired
    public void setPiloteUnitPrice(@Value("${pilotes.configuration.unitPrice}") BigDecimal piloteUnitPrice) {
        this.piloteUnitPrice = piloteUnitPrice;
    }

    @Autowired
    public void setSendToProcessingAfterMinutes(@Value("${pilotes.configuration.sendToProcessingAfterMinutes}") Integer sendToProcessingAfterMinutes) {
        this.sendToProcessingAfterMinutes = sendToProcessingAfterMinutes;
    }

    @Autowired
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private Client createClientIfDoesntExist(ClientDto clientDto) {
        return clientService.getClient(clientDto.getEmail())
                .orElseGet(() -> clientService.createClient(clientDto));
    }

    private Address createAddressIfDoesntExist(AddressDto addressDto) {
        return addressService.getAddress(addressDto)
                .orElseGet(() -> addressService.createAddress(addressDto));
    }

    private Order createNewOrder(RegisterOrderDto orderDto, Client client, Address address) {
        Order order = new ModelMapper().map(orderDto, Order.class);
        order.setState(OrderState.NEW);
        order.setClient(client);
        order.setAddress(address);
        order.setOrderTotal(piloteUnitPrice.multiply(BigDecimal.valueOf(orderDto.getPilotes())));
        order.setCreatedOn(DateUtil.now());
        order.setNumber(UUID.randomUUID().toString());

        log.debug("Creating new Order entity: {}", order);

        return order;
    }

    private void validateOrderOnUpdate(Order existingOrder, OrderDto newOrder) {
        validateOrderCanBeModified(existingOrder);
        validateCustomerHasNotChanged(newOrder.getClient(), existingOrder.getClient().getEmail());
    }

    private void validateOrderCanBeModified(Order order) {
        LocalDateTime modifyExpirationTime = order.getCreatedOn()
                .plusMinutes(sendToProcessingAfterMinutes);

        if (order.getState() != OrderState.NEW
                && DateUtil.now().isAfter(modifyExpirationTime)) {
            log.error("Cannot modify orders created more then {} minutes [Order.createdOn: {}, ExpirationTime: {}] ago",
                    sendToProcessingAfterMinutes, order.getCreatedOn(), modifyExpirationTime);
            throw new OrderModificationDeniedException("Cannot modify orders created more then " + sendToProcessingAfterMinutes + " minutes ago");
        }
    }

    private void validateCustomerHasNotChanged(ClientDto clientDto, String existingClientEmail) {
        if (!clientDto.getEmail().equals(existingClientEmail)) {
            log.error("Cannot change order's client. Existing client: {}, New client: {}",
                    existingClientEmail, clientDto.getEmail());

            throw new OrderProcessingException("Cannot change order's client");
        }
    }

    private void sendOrderProcessingEvent(Order order) {
        if (applicationEventPublisher == null) {
            log.warn("No publisher for events 'OrderReadyToProcessingEvent'");
            return;
        }

        applicationEventPublisher.publishEvent(
                new OrderReadyToProcessingEvent(this, order.getNumber(), order.getPilotes()));
    }
}
