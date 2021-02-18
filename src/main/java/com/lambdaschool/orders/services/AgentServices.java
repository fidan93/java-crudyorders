package com.lambdaschool.orders.services;

import com.lambdaschool.orders.models.Agent;
import com.lambdaschool.orders.repositories.AgentsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

public interface AgentServices
{
    public Agent save(Agent agent);

    Agent findAgentById(long id);
}
