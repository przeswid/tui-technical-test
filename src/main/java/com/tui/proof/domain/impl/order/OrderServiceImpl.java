package com.tui.proof.domain.impl.order;

import com.tui.proof.common.OrderState;
import com.tui.proof.common.util.DateUtil;
import com.tui.proof.domain.api.AddressService;
import com.tui.proof.domain.api.ClientService;
import com.tui.proof.domain.api.OrderService;
import com.tui.proof.dto.AddressDto;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.RegisterOrderDto;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final ClientService clientService;

    private final AddressService addressService;

    private final OrderRepository orderRepository;

    @Value("${pilotes.configuration.unitPrice}")
    private BigDecimal piloteUnitPrice;

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
        order.setOrderTotal(piloteUnitPrice != null ?
                piloteUnitPrice.multiply(BigDecimal.valueOf(orderDto.getPilotes()))
                : null);
        order.setCreatedOn(DateUtil.now());
        order.setNumber(UUID.randomUUID().toString());

        log.debug("Creating new Order entity: {}", order);

        return order;
    }
}
