package com.lambdaschool.orders.repositories;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.views.CustomerOrders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomersRepository extends CrudRepository<Customer,Long>
{
  List<Customer> findCustomerByCustnameContainingIgnoringCase(String subname);

  @Query(value = "SELECT c.custname, count(o.ordnum) AS counts " +
      "FROM customers c LEFT JOIN orders o " +
      "ON c.custcode = o.custcode " +
      "GROUP BY c.custname",nativeQuery = true)

  List <CustomerOrders> getCustomerOrders();
}
