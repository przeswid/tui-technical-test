package com.tui.proof.domain.impl.order;

import com.tui.proof.domain.api.AddressService;
import com.tui.proof.domain.api.ClientService;
import com.tui.proof.dto.AddressDto;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.RegisterOrderDto;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    ClientService clientService;

    @Mock
    AddressService addressService;

    @Mock
    OrderRepository orderRepository;

    @Captor
    ArgumentCaptor<Order> orderCaptor;

    private OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void init() {
        orderServiceImpl = new OrderServiceImpl(clientService, addressService, orderRepository);
    }

    @Test
    void shouldCallRepositorySave_whenCreatingOrder() {
        // Given
        doReturn(Optional.of(new Client())).when(clientService).getClient(anyString());
        doReturn(Optional.of(new Address())).when(addressService).getAddress(Mockito.any(AddressDto.class));

        RegisterOrderDto orderDto = new RegisterOrderDto();
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail("");
        orderDto.setClient(clientDto);
        orderDto.setAddress(new AddressDto());

        // When
        Order order = orderServiceImpl.createOrder(orderDto);

        // Then
        Mockito.verify(orderRepository, times(1)).save(any());
    }

    @Test
    void shouldSaveOrderWithGivenClient_whenCreatingOrder() {
        // Given
        String clientEmail = "client@gmail.com";

        Client client = new Client();
        client.setEmail(clientEmail);
        doReturn(Optional.of(client)).when(clientService).getClient(anyString());
        doReturn(Optional.of(new Address())).when(addressService).getAddress(Mockito.any(AddressDto.class));

        RegisterOrderDto orderDto = new RegisterOrderDto();
        ClientDto clientDto = new ClientDto();
        clientDto.setEmail(clientEmail);

        orderDto.setClient(clientDto);
        orderDto.setAddress(new AddressDto());

        // When
        Order order = orderServiceImpl.createOrder(orderDto);
        Mockito.verify(orderRepository).save(orderCaptor.capture());
        Order savedOrder = orderCaptor.getValue();

        // Then
        assertEquals(savedOrder.getClient().getEmail(), orderDto.getClient().getEmail());
    }
}
