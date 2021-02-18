package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.repositories.AgentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Transactional
@Service(value = "agentServices")
public class AgentServicesImpl implements AgentServices
{
    @Autowired
    private AgentsRepository agentrep;

    @Transactional
    @Override
    public Agent save(Agent agent)
    {
        return agentrep.save(agent);
    }

    @Override
    public Agent findAgentById(long id)
    {
        Agent agent = agentrep.findById(id)
            .orElseThrow(()->new EntityNotFoundException("Agent " + id + " not found"));
        return agent;
    }
}
