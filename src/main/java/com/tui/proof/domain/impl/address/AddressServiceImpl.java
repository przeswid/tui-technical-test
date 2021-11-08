package com.tui.proof.domain.impl.address;

import com.tui.proof.domain.api.AddressService;
import com.tui.proof.dto.AddressDto;
import com.tui.proof.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@Slf4j
class AddressServiceImpl implements AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    public Address createAddress(AddressDto addressDto) {
        log.debug("Creating new address entity: {}", addressDto);
        Address address = new ModelMapper().map(addressDto, Address.class);
        return repository.save(address);
    }

    public Optional<Address> getAddress(AddressDto addressDto) {
        log.debug("Getting address entity by data: {}", addressDto);
        Address address = new ModelMapper().map(addressDto, Address.class);
        return repository.findOne(Example.of(address));
    }

    public Optional<Address> getAddressById(Long id) {
        log.debug("Getting address entity by id: {}", id);
        return repository.findById(id);
    }

}
