package com.projectdummy.dummy.project.controller;

import com.projectdummy.dummy.project.entity.Customer;
import com.projectdummy.dummy.project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping("")
    public String defaultMessage() {
        return "Customer works fine";
    }

    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping(value = "/save",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Customer saveCustomer(@RequestBody Customer customer) {

        return customerService.saveCustomer(customer);
    }

    @PutMapping(value = "/update/{customerCode}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Customer saveCustomer(@RequestBody Customer customer, @PathVariable String customerCode) {
        return customerService.updateCustomer(customer, customerCode);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("/{customercode}")
    public Customer searchByCustomerCode(@PathVariable String customercode) {
        return customerService.searchCustomerByCode(customercode);
    }
}
