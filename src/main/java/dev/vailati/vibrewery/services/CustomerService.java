package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    List<Customer> listCustomers();

    Customer getCustomerById(UUID customerId);
}
