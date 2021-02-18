package com.lambdaschool.orders.repositories;

import com.lambdaschool.orders.models.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface OrderRepository extends CrudRepository<Order,Long>
{
    @Query(value = "SELECT * " +
        "FROM orders o "+
        "WHERE o.advanceamount>0",nativeQuery = true)
    List<Order> getWithAdvanceAmountBiggerThanZero();

}
