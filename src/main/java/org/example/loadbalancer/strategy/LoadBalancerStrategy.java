package org.example.loadbalancer.strategy;

import org.example.loadbalancer.model.ServiceInstance;

import java.util.List;
import java.util.Optional;

public interface LoadBalancerStrategy {
    Optional<ServiceInstance> selectNext(List<ServiceInstance> services);
}
