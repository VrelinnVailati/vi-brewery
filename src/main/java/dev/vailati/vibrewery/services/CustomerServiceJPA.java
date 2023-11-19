package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.mappers.CustomerMapper;
import dev.vailati.vibrewery.model.CustomerDTO;
import dev.vailati.vibrewery.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
       return customerRepository.findAll()
               .stream()
               .map(customerMapper::customerToCustomerDto)
               .toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customerMapper::customerToCustomerDto);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID customerId, CustomerDTO customer) {

    }

    @Override
    public void deleteCustomerById(UUID customerId) {

    }

    @Override
    public void patchCustomerByID(UUID customerId, CustomerDTO customer) {

    }
}
