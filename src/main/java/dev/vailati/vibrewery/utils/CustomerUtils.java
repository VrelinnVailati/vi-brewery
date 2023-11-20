package dev.vailati.vibrewery.utils;

import dev.vailati.vibrewery.entities.Customer;
import org.springframework.util.StringUtils;

public class CustomerUtils {
    public static Customer buildPatchObject(Customer original, Customer updated) {
        Customer builtCustomer = Customer.builder()
                .id(original.getId())
                .version(original.getVersion())
                .createdDate(original.getCreatedDate())
                .lastModifiedDate(original.getLastModifiedDate())
                .build();

        builtCustomer.setCustomerName(StringUtils.hasText(updated.getCustomerName()) ? updated.getCustomerName() : original.getCustomerName());

        return builtCustomer;
    }
}
