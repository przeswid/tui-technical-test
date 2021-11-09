package com.tui.proof.domain.impl.order;

import com.tui.proof.OrderDtoFactory;
import com.tui.proof.common.OrderState;
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
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @Mock
    ApplicationEventPublisher eventPublisher;

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
        orderServiceImpl.setApplicationEventPublisher(eventPublisher);
    }

    @Nested
    class CreateOrderTests {
        @Test
        void shouldCallRepositorySave_whenCreatingOrder() {
            // Given
            RegisterOrderDto orderDto = OrderDtoFactory.createExampleRegisterOrder();
            doReturn(Optional.of(mock(Client.class))).when(clientService).getClient(anyString());
            doReturn(Optional.of(mock(Address.class))).when(addressService).getAddress(any(AddressDto.class));

            // When
            orderServiceImpl.createOrder(orderDto);

            // Then
            Mockito.verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldSaveOrderWithGivenClient_whenCreatingOrder() {
            // Given
            String clientEmail = "client@gmail.com";
            RegisterOrderDto orderDto = OrderDtoFactory.createExampleRegisterOrder();
            orderDto.getClient().setEmail(clientEmail);

            Client client = mock(Client.class);
            doReturn(clientEmail).when(client).getEmail();

            doReturn(Optional.of(client)).when(clientService).getClient(anyString());
            doReturn(Optional.of(mock(Address.class))).when(addressService).getAddress(any(AddressDto.class));

            // When
            orderServiceImpl.createOrder(orderDto);

            // Then
            Mockito.verify(orderRepository).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();
            assertEquals(savedOrder.getClient().getEmail(), orderDto.getClient().getEmail());
        }

        @Test
        void shouldSaveOrderWithPrice10_whenCreatingOrderWith5PilotesAndUnitPrice2() {
            // Given
            Integer pilotesQuantity = 5;
            BigDecimal unitPrice = BigDecimal.valueOf(2.0);
            orderServiceImpl.setPiloteUnitPrice(unitPrice);
            RegisterOrderDto orderDto = OrderDtoFactory.createExampleRegisterOrder();
            orderDto.setPilotes(pilotesQuantity);

            doReturn(Optional.of(mock(Client.class))).when(clientService).getClient(anyString());
            doReturn(Optional.of(mock(Address.class))).when(addressService).getAddress(any(AddressDto.class));

            // When
            orderServiceImpl.createOrder(orderDto);

            // Then
            Mockito.verify(orderRepository).save(orderCaptor.capture());
            Order savedOrder = orderCaptor.getValue();
            assertEquals(savedOrder.getOrderTotal(), unitPrice.multiply(BigDecimal.valueOf(pilotesQuantity)));
        }
    }

    @Nested
    class UpdateOrderTests {
        @Test
        void shouldSaveOrderWithGivenClient_whenUpdatingOrder() {
            // Given
            String clientEmail = "client@email.com";
            OrderDto orderDto = OrderDtoFactory.createExampleOrder();
            orderDto.getClient().setEmail(clientEmail);

            Order orderEntity = mock(Order.class);
            LocalDateTime orderRegisterDate = LocalDateTime.now().minusMinutes(DEFAULT_SEND_TO_PROCESSING_AFTER_MINUTES - 1);
            doReturn(orderRegisterDate).when(orderEntity).getCreatedOn();
            Client clientEntity = mock(Client.class);
            doReturn(clientEmail).when(clientEntity).getEmail();
            doReturn(clientEntity).when(orderEntity).getClient();

            doReturn(Optional.of(orderEntity)).when(orderRepository).findByNumberForUpdate(anyString());

            // When
            orderServiceImpl.updateOrder(orderDto);

            // Then
            Mockito.verify(orderRepository, times(1)).save(any());
        }

        @Test
        void shouldThrowOrderModificationDeniedException_whenModificationTimeExceeded() {
            // Given
            OrderDto orderDto = OrderDtoFactory.createExampleOrder();

            Order orderEntity = mock(Order.class);
            LocalDateTime orderRegisterDate = LocalDateTime.now().minusMinutes(DEFAULT_SEND_TO_PROCESSING_AFTER_MINUTES + 1);
            doReturn(orderRegisterDate).when(orderEntity).getCreatedOn();

            doReturn(Optional.of(orderEntity)).when(orderRepository).findByNumberForUpdate(anyString());

            // When + Then
            assertThrows(OrderModificationDeniedException.class, () -> orderServiceImpl.updateOrder(orderDto));
        }

        @Test
        void shouldThrowOrderProcessingException_whenOrdersClientIsModified() {
            // Given
            String existingClientEmail = "existing@addres.com";
            String newClientEmail = "new@addres.com";

            OrderDto orderDto = OrderDtoFactory.createExampleOrder();
            orderDto.getClient().setEmail(newClientEmail);

            Order orderEntity = mock(Order.class);
            LocalDateTime orderRegisterDate = LocalDateTime.now().minusMinutes(1);
            doReturn(orderRegisterDate).when(orderEntity).getCreatedOn();
            Client clientEntity = mock(Client.class);
            doReturn(existingClientEmail).when(clientEntity).getEmail();
            doReturn(clientEntity).when(orderEntity).getClient();

            doReturn(Optional.of(orderEntity)).when(orderRepository).findByNumberForUpdate(anyString());

            // When + Then
            assertThrows(OrderProcessingException.class, () -> orderServiceImpl.updateOrder(orderDto));
        }
    }

    @Nested
    class SendOrdersToProcessingTests {

        @Test
        void shouldNotSendAnyEvent_whenThereIsNoOrdersToProcess() {
            // Given
            doReturn(Collections.emptyList()).when(orderRepository).findByStateAndCreatedOnLessThan(any(), any());

            // When
            orderServiceImpl.sendOrdersToProcessing();

            // Then
            Mockito.verify(eventPublisher, times(0)).publishEvent(any());
        }

        @Test
        void shouldSendOneEvent_whenThereIsOneOrderToProcess() {
            // Given
            doReturn(List.of(mock(Order.class))).when(orderRepository).findByStateAndCreatedOnLessThan(any(), any());

            // When
            orderServiceImpl.sendOrdersToProcessing();

            // Then
            Mockito.verify(eventPublisher, times(1)).publishEvent(any());
        }

        @Test
        void shouldChangeOrderState_whenThereIsOrderToProcess() {
            // Given
            Order order = mock(Order.class);
            doReturn(List.of(order)).when(orderRepository).findByStateAndCreatedOnLessThan(any(), any());

            // When
            orderServiceImpl.sendOrdersToProcessing();

            // Then
            Mockito.verify(order).setState(OrderState.SENT_TO_PROCESSING);
        }

    }
}
