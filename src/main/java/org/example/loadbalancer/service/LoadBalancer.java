package org.example.loadbalancer.service;

import org.example.loadbalancer.model.ServiceInstance;

import java.util.List;
import java.util.Optional;

public interface LoadBalancer {
    boolean registerService(ServiceInstance serviceInstance);
    boolean unRegisterService(ServiceInstance serviceInstance);
    Optional<ServiceInstance> getNext();
    List<ServiceInstance> getAllServices();
}
