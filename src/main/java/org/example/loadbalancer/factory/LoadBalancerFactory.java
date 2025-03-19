package org.example.loadbalancer.factory;

import org.example.loadbalancer.service.DefaultLoadBalancer;
import org.example.loadbalancer.service.LoadBalancer;
import org.example.loadbalancer.strategy.RandomStrategy;
import org.example.loadbalancer.strategy.RoundRobinStrategy;

public class LoadBalancerFactory {
    public static LoadBalancer createRoundRobinLB() {
        return new DefaultLoadBalancer(new RoundRobinStrategy());
    }

    public static LoadBalancer createRandomLB() {
        return new DefaultLoadBalancer(new RandomStrategy());
    }
}
