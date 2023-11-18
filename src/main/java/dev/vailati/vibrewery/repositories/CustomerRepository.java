package dev.vailati.vibrewery.repositories;

import dev.vailati.vibrewery.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
