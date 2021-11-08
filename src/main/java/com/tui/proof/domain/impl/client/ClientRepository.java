package com.tui.proof.domain.impl.client;

import com.tui.proof.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
}
