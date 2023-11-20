package dev.vailati.vibrewery.controller;

import dev.vailati.vibrewery.entities.Customer;
import dev.vailati.vibrewery.mappers.CustomerMapper;
import dev.vailati.vibrewery.model.CustomerDTO;
import dev.vailati.vibrewery.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void testListCustomers() {
        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(2);

        for (CustomerDTO dto : dtos) {
            assertThat(dto).isNotNull();
            assertThat(dto.getId()).isNotNull();
        }
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyListCustomers() {
        customerRepository.deleteAll();

        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(customer.getId());
    }

    @Test
    void testGetCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testSaveCustomer() {
        final String customerName = "New Customer";

        CustomerDTO customer = CustomerDTO.builder()
                .customerName(customerName)
                .build();

        ResponseEntity<CustomerDTO> responseEntity = customerController.createCustomer(customer);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String uri = responseEntity.getHeaders().getLocation().getPath();
        UUID savedUUID = UUID.fromString(uri.split("/")[4]);

        Customer createdCustomer = customerRepository.findById(savedUUID).get();

        assertThat(createdCustomer).isNotNull();
        assertThat(createdCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        final String customerName = "Updated customer";

        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setCustomerName(customerName);

        ResponseEntity<Void> responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class,
                () -> customerController.updateCustomerById(UUID.randomUUID(),
                CustomerDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerById() {
        Customer customer = customerRepository.findAll().get(0);

        ResponseEntity<Void> responseEntity = customerController.deleteCustomerById(customer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testDeleteNotFoundCustomer() {
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        final String customerName = "Updated customer";

        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setCustomerName(customerName);

        ResponseEntity<Void> responseEntity = customerController.patchCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));

        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo(customerName);
    }

    @Test
    void testPatchNotFoundCustomer() {
        assertThrows(NotFoundException.class, () -> customerController.patchCustomerById(
                UUID.randomUUID(),
                CustomerDTO.builder().build()
        ));
    }
}