package org.example.loadbalancer.strategy;

import org.example.loadbalancer.model.ServiceInstance;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomStrategy implements LoadBalancerStrategy {
    private final Random random = new Random();

    @Override
    public Optional<ServiceInstance> selectNext(List<ServiceInstance> services) {
        if (services == null || services.isEmpty()) {
            return Optional.empty();
        }

        int index = random.nextInt(services.size());

        return Optional.of(services.get(index));
    }
}
