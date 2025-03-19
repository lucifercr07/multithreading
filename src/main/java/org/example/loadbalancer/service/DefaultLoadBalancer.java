package org.example.loadbalancer.service;

import org.example.loadbalancer.model.ServiceInstance;
import org.example.loadbalancer.strategy.LoadBalancerStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultLoadBalancer implements LoadBalancer {
    public static final int MAX_SERVICES = 10;

    private final List<ServiceInstance> serviceInstances;
    private final LoadBalancerStrategy strategy;
    private final ReadWriteLock lock;

    public DefaultLoadBalancer(LoadBalancerStrategy strategy) {
        this.serviceInstances = new ArrayList<>(MAX_SERVICES);
        this.strategy = strategy;
        this.lock = new ReentrantReadWriteLock();
    }


    @Override
    public boolean registerService(ServiceInstance serviceInstance) {
        if (serviceInstance == null) {
            return false;
        }

        lock.writeLock().lock();
        try {
            if (serviceInstances.size() >= MAX_SERVICES) {
                throw new IllegalStateException("Service registry full. Cannot register more than " + MAX_SERVICES + " services.");
            }

            // Check for duplicates
            if (serviceInstances.contains(serviceInstance)) {
                return false;
            }

            return serviceInstances.add(serviceInstance);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean unRegisterService(ServiceInstance serviceInstance) {
        if (serviceInstance == null) {
            return false;
        }

        lock.writeLock().lock();
        try {
            return serviceInstances.remove(serviceInstance);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<ServiceInstance> getNext() {
        lock.readLock().lock();
        try {
            return strategy.selectNext(new ArrayList<>(serviceInstances));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<ServiceInstance> getAllServices() {
        lock.readLock().lock();
        try {
            return List.copyOf(serviceInstances);
        } finally {
            lock.readLock().unlock();
        }
    }
}
