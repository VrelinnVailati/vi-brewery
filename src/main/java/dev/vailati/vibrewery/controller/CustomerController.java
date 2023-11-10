package dev.vailati.vibrewery.controller;

import dev.vailati.vibrewery.model.Customer;
import dev.vailati.vibrewery.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private CustomerService customerService;

    @PatchMapping("{customerId}")
    public ResponseEntity<Void> patchCustomerById(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.patchCustomerByID(customerId, customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable UUID customerId) {
        customerService.deleteCustomerById(customerId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<Void> updateCustomerById(@PathVariable UUID customerId, @RequestBody Customer customer) {
        customerService.updateCustomerById(customerId, customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId());

        return new ResponseEntity<>(savedCustomer, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping("{customerId}")
    public Customer getCustomerById(@PathVariable UUID customerId) {
        return customerService.getCustomerById(customerId);
    }
}
