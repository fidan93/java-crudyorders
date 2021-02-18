package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.models.Payment;
import com.lambdaschool.orders.repositories.CustomersRepository;
import com.lambdaschool.orders.repositories.OrderRepository;
import com.lambdaschool.orders.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service(value = "orderServices")
public class OrderServiceImpl implements OrderServices
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    @Override
    public Order save(Order tempOrder)
    {
        Order newOrder =  new Order();
        //put
        if(tempOrder.getOrdnum()!= 0){
            orderRepository.findById(tempOrder.getOrdnum())
                .orElseThrow(()->new EntityNotFoundException("Order" + tempOrder.getOrdnum()+ " not found"));
            newOrder.setOrdnum(tempOrder.getOrdnum());
        }
        //post
        newOrder.setOrdamount(tempOrder.getOrdamount());
        newOrder.setOrderdescription(tempOrder.getOrderdescription());
        newOrder.setAdvanceamount(tempOrder.getAdvanceamount());

        newOrder.setCustomer(customersRepository.findById(tempOrder.getCustomer().getCustcode())
        .orElseThrow(() -> new EntityNotFoundException("Customer " + tempOrder.getCustomer().getCustcode() + " not found!")));

        newOrder.getPayments().clear();
        for(Payment p: tempOrder.getPayments())
        {
            Payment payment = paymentRepository.findById(p.getPaymentid())
                .orElseThrow(() -> new EntityNotFoundException("Payment type " + p.getPaymentid() + " not found"));

            newOrder.getPayments().add(payment);
        }
        return orderRepository.save(newOrder);
    }

    @Override
    public Order findOrderById(long id)
    {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Order "+ id + " Not found"));
        return order;
    }

    @Override
    public List<Order> getAdvanceAmount()
    {
      List<Order> orderList = orderRepository.getWithAdvanceAmountBiggerThanZero();
      return orderList;
    }

    @Override
    public void deleteOrderById(long id)
    {
        if(orderRepository.findById(id).isPresent())
        {
            orderRepository.deleteById(id);
        }
        else
        {
            throw new EntityNotFoundException("Order " + id+ " not found");
        }
    }
}
