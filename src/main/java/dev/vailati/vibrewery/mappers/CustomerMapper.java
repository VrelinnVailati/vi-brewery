package dev.vailati.vibrewery.mappers;

import dev.vailati.vibrewery.entities.Customer;
import dev.vailati.vibrewery.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
