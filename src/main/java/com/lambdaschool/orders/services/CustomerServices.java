package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.views.CustomerOrders;

import java.util.List;

public interface CustomerServices
{


    List <Customer> findAllCustomers();

    Customer  findCustomerById(long id);

    List<Customer> findCustomerByLikeName(String subname);
    List<CustomerOrders> getCustomerOrdersCount();

    Customer save(Customer customer);
    Customer update(long id,Customer customer);
    void deleteById(long id);

}
