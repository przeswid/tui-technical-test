package com.tui.proof.domain.api;

import com.tui.proof.dto.ClientDto;
import com.tui.proof.model.Client;

import java.util.Optional;

public interface ClientService {
    Client createClient(ClientDto clientDto);

    Optional<Client> getClient(String email);

    void addAddressToClient(Long addressId, Long clientId);

}
