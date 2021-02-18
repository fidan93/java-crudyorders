package com.lambdaschool.orders.controllers;

import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.services.CustomerServices;
import com.lambdaschool.orders.views.CustomerOrders;
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
@RequestMapping(value="/customers")
public class CustomerControllers
{
    @Autowired
    private CustomerServices customerServices;
   // http://localhost:2019/customers/orders
   @GetMapping(value = "/orders")
    public ResponseEntity<?> findAllCustomers()
   {
       List<Customer> customerList = customerServices.findAllCustomers();
       return new ResponseEntity<>(customerList,
           HttpStatus.OK);
   }
//    http://localhost:2019/customers/customer/7
    @GetMapping(value = "customer/{custid}",produces = "application/json")
    public ResponseEntity<?> findCustomerById(@PathVariable long custid)
    {
        Customer customer = customerServices.findCustomerById(custid);
        return new ResponseEntity<>(customer,HttpStatus.OK);
    }
//    http://localhost:2019/customers/customer/77
//    http://localhost:2019/customers/namelike/mes
    @GetMapping(value = "/namelike/{subname}",produces = "application/json")
    public ResponseEntity<?> findCustomerByLikeName(@PathVariable String subname)
    {
      List<Customer> customerList = customerServices.findCustomerByLikeName(subname);
      return new ResponseEntity<>(customerList,HttpStatus.OK);
    }


//    /customers/orders/count
    @GetMapping(value = "/orders/count",produces = "application/json")
    public ResponseEntity<?> getCustomerOrdersCount()
    {
        List <CustomerOrders> customerOrders = customerServices.getCustomerOrdersCount();
        return new ResponseEntity<>(customerOrders,HttpStatus.OK);
    }

    @PostMapping(value = "/customer", consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> addNewCustomer(@Valid @RequestBody Customer customer)
    {
        customer.setCustcode(0);
        customer = customerServices.save(customer);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newCustomerUri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{custid}")
            .buildAndExpand(customer.getCustcode())
            .toUri();
        responseHeaders.setLocation(newCustomerUri);

        return new ResponseEntity<>(customer,responseHeaders,HttpStatus.CREATED);
    }
    @PutMapping(value="/customer/{custid}",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> replaceCustomer(@Valid @RequestBody Customer customer, @PathVariable long custid)
    {
        customer.setCustcode(custid);
        Customer newCustomer = customerServices.save(customer);
        return  new ResponseEntity<>(newCustomer,HttpStatus.OK);
    }

    @PatchMapping(value = "/customer/{custid}",consumes = "application/json",produces = "application/json")
    public ResponseEntity<?> changeCustomerById(@RequestBody Customer customer, @PathVariable long custid)
    {
        Customer newCustomer = customerServices.update(custid,customer);

        return new ResponseEntity<>(newCustomer,HttpStatus.OK);
    }

    @DeleteMapping(value = "/customer/{custid}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable long custid)
    {
        customerServices.deleteById(custid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
