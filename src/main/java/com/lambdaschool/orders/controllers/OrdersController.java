package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.services.OrderServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrdersController
{
    @Autowired
    private OrderServices orderServices;

    @GetMapping(value = "/order/{orderid}",produces = "application/json")
    public ResponseEntity<?> findOrderById(@PathVariable long orderid)
    {
        Order order = orderServices.findOrderById(orderid);
        return new ResponseEntity<>(order,
            HttpStatus.OK);
    }
    @GetMapping(value = "/advanceamount")
    public ResponseEntity<?> getAdvanceAmount()
    {
        List<Order> advanceAmounts = orderServices.getAdvanceAmount();
        return new ResponseEntity<>(advanceAmounts,HttpStatus.OK);
    }
    @PostMapping(value = "/order",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> addNewOrder(@Valid @RequestBody Order order)
    {
        order.setOrdnum(0);
        order = orderServices.save(order);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newOrderURI = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{ordnum}")
        .buildAndExpand(order.getOrdnum())
         .toUri();
        responseHeaders.setLocation(newOrderURI);

        return  new ResponseEntity<>(order,responseHeaders,HttpStatus.CREATED);
    }
    @PutMapping(value="/order/{ordid}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> replaceOrder(@Valid @RequestBody Order order,@PathVariable long ordid)
    {
        order.setOrdnum(ordid);
        order = orderServices.save(order);

        return new ResponseEntity<>(order,HttpStatus.OK);
    }
    @DeleteMapping(value = "/order/{ordid}")
    public ResponseEntity <?> deleteOrderById(@PathVariable long ordid)
    {
        orderServices.deleteOrderById(ordid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
