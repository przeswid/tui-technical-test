package com.tui.proof.domain.impl.order;

import com.tui.proof.common.exception.OrderModificationDeniedException;
import com.tui.proof.common.exception.OrderProcessingException;
import com.tui.proof.domain.api.AddressService;
import com.tui.proof.domain.api.ClientService;
import com.tui.proof.dto.AddressDto;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.OrderDto;
import com.tui.proof.dto.RegisterOrderDto;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import com.tui.proof.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private static final BigDecimal DEFAULT_PLIOTES_UNIT_PRICE = BigDecimal.valueOf(2.0);
    private static final Integer DEFAULT_SEND_TO_PROCESSING_AFTER_MINUTES = 5;

    @BeforeEach
    void init() {
        orderServiceImpl = new OrderServiceImpl(clientService, addressService, orderRepository);
        orderServiceImpl.setPiloteUnitPrice(DEFAULT_PLIOTES_UNIT_PRICE);
        orderServiceImpl.setSendToProcessingAfterMinutes(DEFAULT_SEND_TO_PROCESSING_AFTER_MINUTES);
    }

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCallRepositorySave_whenCreatingOrder() {
            // Given
            doReturn(Optional.of(mock(Client.class))).when(clientService).getClient(anyString());
            doReturn(Optional.of(mock(Address.class))).when(addressService).getAddress(Mockito.any(AddressDto.class));

            RegisterOrderDto orderDto = new RegisterOrderDto();
            orderDto.setPilotes(1);
            ClientDto clientDto = new ClientDto();
            clientDto.setEmail("");
            orderDto.setClient(clientDto);
            orderDto.setAddress(new AddressDto());

            // When
            orderServiceImpl.createOrder(orderDto);

            // Then
            Mockito.verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldSaveOrderWithGivenClient_whenCreatingOrder() {
            // Given
            String clientEmail = "client@gmail.com";

            Client client = mock(Client.class);
            doReturn(clientEmail).when(client).getEmail();

            doReturn(Optional.of(client)).when(clientService).getClient(anyString());
            doReturn(Optional.of(mock(Address.class))).when(addressService).getAddress(Mockito.any(AddressDto.class));

            RegisterOrderDto orderDto = new RegisterOrderDto();
            orderDto.setPilotes(1);
            ClientDto clientDto = new ClientDto();
            clientDto.setEmail(clientEmail);

            orderDto.setClient(clientDto);
            orderDto.setAddress(new AddressDto());

            // When
            orderServiceImpl.createOrder(orderDto);
            Mockito.verify(orderRepository).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();

            // Then
            assertEquals(savedOrder.getClient().getEmail(), orderDto.getClient().getEmail());
        }

        @Test
        void shouldSaveOrderWithPrice10_whenCreatingOrderWith5PilotesAndUnitPrice2() {
            // Given
            Integer pilotesQuantity = 5;

            doReturn(Optional.of(mock(Client.class))).when(clientService).getClient(anyString());
            doReturn(Optional.of(mock(Address.class))).when(addressService).getAddress(Mockito.any(AddressDto.class));

            RegisterOrderDto orderDto = new RegisterOrderDto();
            ClientDto clientDto = new ClientDto();
            clientDto.setEmail("");
            orderDto.setClient(clientDto);
            orderDto.setAddress(new AddressDto());
            orderDto.setPilotes(pilotesQuantity);

            // When
            orderServiceImpl.createOrder(orderDto);
            Mockito.verify(orderRepository).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();

            // Then
            assertEquals(savedOrder.getOrderTotal(), BigDecimal.valueOf(10.0));
        }
    }

    @Nested
    class UpdateOrderTests {
        @Test
        void shouldSaveOrderWithGivenClient_whenUpdatingOrder() {
            // Given
            String clientEmail = "client@email.com";

            Order orderEntity = mock(Order.class);
            LocalDateTime orderRegisterDate = LocalDateTime.now().minusMinutes(DEFAULT_SEND_TO_PROCESSING_AFTER_MINUTES - 1);
            doReturn(orderRegisterDate).when(orderEntity).getCreatedOn();
            Client clientEntity = mock(Client.class);
            doReturn(clientEmail).when(clientEntity).getEmail();
            doReturn(clientEntity).when(orderEntity).getClient();

            doReturn(Optional.of(orderEntity)).when(orderRepository).findByNumber(anyString());

            OrderDto orderDto = new OrderDto();
            orderDto.setNumber("order_number");
            orderDto.setPilotes(5);
            ClientDto clientDto = new ClientDto();
            clientDto.setEmail(clientEmail);
            orderDto.setClient(clientDto);
            orderDto.setAddress(new AddressDto());

            // When
            orderServiceImpl.updateOrder(orderDto);

            // Then
            Mockito.verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldThrowOrderModificationDeniedException_whenModificationTimeExceeded() {
            // Given
            Order orderEntity = mock(Order.class);
            LocalDateTime orderRegisterDate = LocalDateTime.now().minusMinutes(DEFAULT_SEND_TO_PROCESSING_AFTER_MINUTES + 1);
            doReturn(orderRegisterDate).when(orderEntity).getCreatedOn();

            doReturn(Optional.of(orderEntity)).when(orderRepository).findByNumber(anyString());

            OrderDto orderDto = new OrderDto();
            orderDto.setNumber("order_number");
            orderDto.setPilotes(5);
            ClientDto clientDto = new ClientDto();
            clientDto.setEmail("");
            orderDto.setClient(clientDto);
            orderDto.setAddress(new AddressDto());

            // When + Then
            assertThrows(OrderModificationDeniedException.class, () -> orderServiceImpl.updateOrder(orderDto));
        }

        @Test
        void shouldThrowOrderProcessingException_whenOrdersClientIsModified() {
            // Given
            String existingClientEmail = "existing@addres.com";
            String newClientEmail = "new@addres.com";

            Order orderEntity = mock(Order.class);
            LocalDateTime orderRegisterDate = LocalDateTime.now().minusMinutes(1);
            doReturn(orderRegisterDate).when(orderEntity).getCreatedOn();
            Client clientEntity = mock(Client.class);
            doReturn(existingClientEmail).when(clientEntity).getEmail();
            doReturn(clientEntity).when(orderEntity).getClient();

            doReturn(Optional.of(orderEntity)).when(orderRepository).findByNumber(anyString());

            OrderDto orderDto = new OrderDto();
            orderDto.setNumber("order_number");
            ClientDto clientDto = new ClientDto();
            clientDto.setEmail(newClientEmail);
            orderDto.setClient(clientDto);
            orderDto.setAddress(new AddressDto());

            // When + Then
            assertThrows(OrderProcessingException.class, () -> orderServiceImpl.updateOrder(orderDto));
        }
    }

}
