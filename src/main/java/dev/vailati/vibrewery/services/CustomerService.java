package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<CustomerDTO> listCustomers();

    Optional<CustomerDTO> getCustomerById(UUID customerId);

    CustomerDTO saveCustomer(CustomerDTO customer);

    Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer);

    boolean deleteCustomerById(UUID customerId);

    Optional<CustomerDTO> patchCustomerByID(UUID customerId, CustomerDTO customer);
}
