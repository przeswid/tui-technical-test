package com.tui.proof;

import com.tui.proof.dto.AddressDto;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.dto.OrderDto;
import com.tui.proof.dto.RegisterOrderDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OrderDtoFactory {

    public static RegisterOrderDto createExampleRegisterOrder() {
        RegisterOrderDto order = new RegisterOrderDto();

        order.setPilotes(5);

        ClientDto client = new ClientDto();
        client.setEmail("email@email.com");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setTelephone("+48 123 23 23");

        order.setClient(client);

        AddressDto address = new AddressDto();
        address.setStreet("street");
        address.setCity("city");
        address.setCountry("country");
        address.setPostcode("12-123");
        address.setHouseNumber("12A");

        order.setAddress(address);

        return order;
    }

    public static OrderDto createExampleOrder() {
        OrderDto order = new OrderDto();

        order.setPilotes(5);
        order.setNumber("123-456-789");

        ClientDto client = new ClientDto();
        client.setEmail("email@email.com");
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setTelephone("+48 123 23 23");

        order.setClient(client);

        AddressDto address = new AddressDto();
        address.setStreet("street");
        address.setCity("city");
        address.setCountry("country");
        address.setPostcode("12-123");
        address.setHouseNumber("12A");

        order.setAddress(address);

        return order;
    }
}
