package com.tui.proof.domain.impl.client;

import com.tui.proof.common.exception.ResourceNotFoundException;
import com.tui.proof.domain.api.AddressService;
import com.tui.proof.domain.api.ClientService;
import com.tui.proof.dto.ClientDto;
import com.tui.proof.model.Address;
import com.tui.proof.model.Client;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    private final AddressService addressService;

    @Autowired
    public ClientServiceImpl(ClientRepository repository, AddressService addressService) {
        this.repository = repository;
        this.addressService = addressService;
    }

    public Client createClient(ClientDto clientDto) {
        log.debug("Creating new client entity: {}", clientDto);
        Client client = new ModelMapper().map(clientDto, Client.class);
        return repository.save(client);
    }

    public Optional<Client> getClient(String email) {
        log.debug("Getting client entity by e-mail: {}", email);
        return repository.findByEmail(email);
    }

    public void addAddressToClient(Long addressId, Long clientId) {
        log.debug("Trying to connect new address (id: {}) to client (id: {})", addressId, clientId);
        Address address = addressService.getAddressById(addressId).orElseThrow(() -> new ResourceNotFoundException(clientId));
        Client client = repository.findById(clientId).orElseThrow(() -> new ResourceNotFoundException(clientId));

        boolean addressNotAssignedYet = client
                .getAddresses()
                .stream()
                .map(Address::getId)
                .noneMatch(id -> Objects.equals(id, addressId));

        if (addressNotAssignedYet) {
            log.debug("Address has been added");
            client.getAddresses().add(address);
        } else {
            log.debug("Address already assigned");
        }
    }

}