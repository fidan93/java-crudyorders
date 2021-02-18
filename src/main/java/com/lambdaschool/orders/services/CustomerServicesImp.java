package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.models.Customer;
import com.lambdaschool.orders.models.Order;
import com.lambdaschool.orders.models.Payment;
import com.lambdaschool.orders.repositories.AgentsRepository;
import com.lambdaschool.orders.repositories.CustomersRepository;
import com.lambdaschool.orders.repositories.PaymentRepository;
import com.lambdaschool.orders.views.CustomerOrders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "customerServices")
public class CustomerServicesImp implements CustomerServices
{
    @Autowired
    private CustomersRepository customerrep;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AgentsRepository agentsRepository;

    @Transactional
    @Override
    public Customer save(Customer tempCustomer)
    {
        Customer newCustomer = new Customer();

        if(tempCustomer.getCustcode()!= 0){//put
            customerrep.findById(tempCustomer.getCustcode())
                .orElseThrow(() -> new EntityNotFoundException("Customer "+ tempCustomer.getCustcode()+ " not found!"));
            newCustomer.setCustcode(tempCustomer.getCustcode());
        }

        newCustomer.setCustname(tempCustomer.getCustname());
        newCustomer.setCustcity(tempCustomer.getCustcity());
        newCustomer.setCustcountry(tempCustomer.getCustcountry());
        newCustomer.setGrade(tempCustomer.getGrade());
        newCustomer.setOpeningamt(tempCustomer.getOpeningamt());
        newCustomer.setOutstandingamt(tempCustomer.getOutstandingamt());
        newCustomer.setPaymentamt(tempCustomer.getPaymentamt());
        newCustomer.setPhone(tempCustomer.getPhone());
        newCustomer.setReceiveamt(tempCustomer.getReceiveamt());
        newCustomer.setWorkingarea(tempCustomer.getWorkingarea());

        //Agent
        Agent a = tempCustomer.getAgent();
        Agent newAgent = agentsRepository.findById(a.getAgentcode())
            .orElseThrow(() -> new EntityNotFoundException("Agent "+a.getAgentcode()+" not found"));

        newCustomer.setAgent(newAgent);
        //orders
        newCustomer.getOrders().clear();
        for(Order o: tempCustomer.getOrders())
        {
            Order newOrder = new Order();
            newOrder.setAdvanceamount(o.getAdvanceamount());
            newOrder.setOrdamount(o.getOrdamount());
            newOrder.setOrderdescription(o.getOrderdescription());
            newOrder.setCustomer(newCustomer);

            newOrder.getPayments().clear();
            for(Payment p: o.getPayments())
            {
                Payment payment = paymentRepository.findById(p.getPaymentid())
                    .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid()+" not found!"));

                newOrder.getPayments().add(payment);
            }

            newCustomer.getOrders().add(newOrder);
        }

        return customerrep.save(newCustomer);
    }

    @Transactional
    @Override
    public Customer update(long id,Customer tempCustomer)
    {
        Customer updateCustomer =  customerrep.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer "+ id + " not found!"));

         if(tempCustomer.getCustname()!= null){updateCustomer.setCustname(tempCustomer.getCustname());}
         if(tempCustomer.getCustcity()!=null){ updateCustomer.setCustcity(tempCustomer.getCustcity());}
         if(tempCustomer.getCustcountry()!=null){ updateCustomer.setCustcountry(tempCustomer.getCustcountry());}
         if(tempCustomer.getGrade()!=null){ updateCustomer.setGrade(tempCustomer.getGrade()); }
         if(tempCustomer.hasvalueforopeningamt){ updateCustomer.setOpeningamt(tempCustomer.getOpeningamt());}
         if(tempCustomer.hasvalueforoutstandingamt){ updateCustomer.setOutstandingamt(tempCustomer.getOutstandingamt());}
         if(tempCustomer.hasvalueforpaymentamt){ updateCustomer.setPaymentamt(tempCustomer.getPaymentamt()); }
         if(tempCustomer.getPhone()!=null){ updateCustomer.setPhone(tempCustomer.getPhone());}
         if(tempCustomer.hasvalueforreceiveamt){ updateCustomer.setReceiveamt(tempCustomer.getReceiveamt()); }
         if(tempCustomer.getWorkingarea()!=null){ updateCustomer.setWorkingarea(tempCustomer.getWorkingarea()); }

        //Agent
        if(tempCustomer.getAgent()!=null)
        {
            Agent a = tempCustomer.getAgent();
            Agent newAgent = agentsRepository.findById(a.getAgentcode())
                .orElseThrow(() -> new EntityNotFoundException("Agent " + a.getAgentcode() + " not found"));

            updateCustomer.setAgent(newAgent);
        }
        //orders
        if(tempCustomer.getOrders().size()>0)
        {
            updateCustomer.getOrders().clear();
            
            for (Order o : tempCustomer.getOrders())
            {
                Order newOrder = new Order();
                newOrder.setAdvanceamount(o.getAdvanceamount());
                newOrder.setOrdamount(o.getOrdamount());
                newOrder.setOrderdescription(o.getOrderdescription());
                newOrder.setCustomer(updateCustomer);

                if(o.getPayments().size()>0)
                {
                    newOrder.getPayments()
                        .clear();
                    for (Payment p : o.getPayments())
                    {
                        Payment payment = paymentRepository.findById(p.getPaymentid())
                            .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid() + " not found!"));

                        newOrder.getPayments()
                            .add(payment);
                    }
                }
                updateCustomer.getOrders()
                    .add(newOrder);
            }
        }
      return customerrep.save(updateCustomer);
    }


    @Override
    public List<Customer> findAllCustomers()
    {
        List <Customer> customerList = new ArrayList<>();
        customerrep.findAll().iterator().forEachRemaining(customerList::add);
        return customerList;

    }

    @Override
    public Customer findCustomerById(long id)
    {
       Customer customer = customerrep.findById(id)
           .orElseThrow(()-> new EntityNotFoundException("Customer "+id+" not found"));
       return  customer;
    }

    @Override
    public List<Customer> findCustomerByLikeName(String subname)
    {
        List <Customer> customerList  = customerrep.findCustomerByCustnameContainingIgnoringCase(subname);
        return customerList;
    }

    @Override
    public List<CustomerOrders> getCustomerOrdersCount()
    {
        List <CustomerOrders> customerOrders = customerrep.getCustomerOrders();
        return customerOrders;
    }

    @Override
    public void deleteById(long id)
    {
        if(customerrep.findById(id).isPresent())
        {
            customerrep.deleteById(id);
        }
        else
        {
            throw new EntityNotFoundException("Customer "+id+ " not found!");
        }
    }
}
