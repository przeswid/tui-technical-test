package com.tui.proof.domain.api;

import com.tui.proof.dto.AddressDto;
import com.tui.proof.model.Address;

import java.util.Optional;

public interface AddressService {

    Address createAddress(AddressDto addressDto);

    Optional<Address> getAddress(AddressDto addressDto);

    Optional<Address> getAddressById(Long id);
}
