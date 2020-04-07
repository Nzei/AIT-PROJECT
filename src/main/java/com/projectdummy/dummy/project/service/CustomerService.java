package com.projectdummy.dummy.project.service;

import com.projectdummy.dummy.project.Scanner.SecugenScanner;
import com.projectdummy.dummy.project.entity.Customer;
import com.projectdummy.dummy.project.repository.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private ICustomerRepository customerRepository;


    @Autowired
    public CustomerService(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer saveCustomer(Customer customer) {
        customer.setCustomerCode(generateID(customer));
        customer.setBalance(5000);
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer searchCustomerByCode(String customerCode) {
        return customerRepository.findByCustomerCode(customerCode);
    }

    public Customer updateCustomer(Customer customer, String customerCode) {
        Customer dbCust = searchCustomerByCode(customerCode);
        if (dbCust != null) {
            dbCust.setPhoneNumber(customer.getPhoneNumber());
            dbCust.setAddress(customer.getAddress());
            dbCust.setCustomerCode(customer.getCustomerCode());
            dbCust.setFirstName(customer.getFirstName());
            dbCust.setLastName(customer.getLastName());
            dbCust.setBalance(customer.getBalance());
            dbCust.setDob(customer.getDob());
            dbCust.setEmail(customer.getEmail());
            dbCust.setMothersMaidenName(customer.getMothersMaidenName());
            dbCust.setGender(customer.getGender());
            dbCust.setAccountNumber(customer.getAccountNumber());
        } else {
            System.out.println("Invalid entry");
        }
        return saveCustomer(dbCust);
    }

    public String generateID(Customer customer) {
        String UID = customer.getFirstName().substring(0, 1).toUpperCase() + customer.getLastName().substring(0, 1).toUpperCase() + System.currentTimeMillis();
        return UID;
    }


    public void matchingOfPrints(String UID) {
        Customer searchedCustomer = searchCustomerByCode(UID);
        if (searchedCustomer != null) {
            searchedCustomer.getFingerprints();
        } else {
            System.out.println("No customer found with " + UID);
        }
        SecugenScanner secugenScanner = new SecugenScanner();
        secugenScanner.start();
        byte[] image1 = secugenScanner.captureBytes();
        assert searchedCustomer != null;
    }
}
