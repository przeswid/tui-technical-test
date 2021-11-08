package com.tui.proof.domain.impl.address;

import com.tui.proof.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStreet(String street);
}
