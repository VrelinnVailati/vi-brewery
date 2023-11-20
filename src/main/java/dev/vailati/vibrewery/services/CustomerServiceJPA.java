package dev.vailati.vibrewery.services;

import dev.vailati.vibrewery.mappers.CustomerMapper;
import dev.vailati.vibrewery.model.CustomerDTO;
import dev.vailati.vibrewery.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
        return customerMapper.customerToCustomerDto(
                customerRepository.save(customerMapper.customerDtoToCustomer(customer))
        );
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(
                existingCustomer -> {
                    existingCustomer.setCustomerName(customer.getCustomerName());

                    customerRepository.save(existingCustomer);
                    atomicReference.set(Optional.of(customerMapper.customerToCustomerDto(existingCustomer)));
                },
                () -> {
                    atomicReference.set(Optional.empty());
                });

        return atomicReference.get();
    }

    @Override
    public boolean deleteCustomerById(UUID customerId) {
        if(customerRepository.existsById(customerId)) {
            customerRepository.deleteById(customerId);

            return true;
        }

        return false;
    }

    @Override
    public void patchCustomerByID(UUID customerId, CustomerDTO customer) {

    }
}
