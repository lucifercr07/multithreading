package org.example.loadbalancer;

import org.example.loadbalancer.factory.LoadBalancerFactory;
import org.example.loadbalancer.model.ServiceInstance;
import org.example.loadbalancer.service.LoadBalancer;

public class LoadbalancerMain {
    public static void main(String[] args) {
        // Create a round-robin load balancer
        System.out.println("=== Round Robin Load Balancer ===");
        LoadBalancer roundRobinLB = LoadBalancerFactory.createRoundRobinLB();

        // Register services
        roundRobinLB.registerService(new ServiceInstance("1", "service1.example.com", 8001));
        roundRobinLB.registerService(new ServiceInstance("2", "service2.example.com", 8002));
        roundRobinLB.registerService(new ServiceInstance("3", "service3.example.com", 8003));

        // Demonstrate round-robin selection
        for (int i = 0; i < 10; i++) {
            System.out.println("Next service: " + roundRobinLB.getNext().get().getId());
        }

        // Create a random load balancer
        System.out.println("\n=== Random Load Balancer ===");
        LoadBalancer randomLB = LoadBalancerFactory.createRandomLB();

        // Register services
        randomLB.registerService(new ServiceInstance("A", "serviceA.example.com", 9001));
        randomLB.registerService(new ServiceInstance("B", "serviceB.example.com", 9002));
        randomLB.registerService(new ServiceInstance("C", "serviceC.example.com", 9003));

        // Demonstrate random selection
        for (int i = 0; i < 10; i++) {
            System.out.println("Next service: " + randomLB.getNext().get().getId());
        }
    }
}
