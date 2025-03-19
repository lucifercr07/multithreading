package org.example.loadbalancer.strategy;

import org.example.loadbalancer.model.ServiceInstance;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinStrategy implements LoadBalancerStrategy {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Optional<ServiceInstance> selectNext(List<ServiceInstance> services) {
        if (services == null || services.isEmpty()) {
            return Optional.empty();
        }

        int index = counter.getAndIncrement() % services.size();
        if (index < 0) {
            counter.set(0);
            index = 0;
        }

        return Optional.of(services.get(index));
    }
}
