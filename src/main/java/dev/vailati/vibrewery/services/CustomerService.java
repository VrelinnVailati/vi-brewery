package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();

    Optional<Customer> getCustomerById(UUID customerId);

    Customer saveCustomer(Customer customer);

    void updateCustomerById(UUID customerId, Customer customer);

    void deleteCustomerById(UUID customerId);

    void patchCustomerByID(UUID customerId, Customer customer);
}
