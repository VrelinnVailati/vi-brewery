package dev.vailati.vibrewery.controller;

import dev.vailati.vibrewery.model.Customer;
import dev.vailati.vibrewery.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private CustomerService customerService;

    @GetMapping
    List<Customer> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping("{customerId}")
    Customer getCustomerById(@PathVariable UUID customerId) {
        return customerService.getCustomerById(customerId);
    }
}
