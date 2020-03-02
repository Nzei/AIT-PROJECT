package com.projectdummy.dummy.project.repository;


import com.projectdummy.dummy.project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findByCustomerCode(String customerCode);

    public List<Customer> findByLastNameOrFirstName(String lastName, String firstName);

    public Customer findByPhoneNumber(String phoneNumber);
}
