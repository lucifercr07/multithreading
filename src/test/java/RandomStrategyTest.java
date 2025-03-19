import org.example.loadbalancer.factory.LoadBalancerFactory;
import org.example.loadbalancer.model.ServiceInstance;
import org.example.loadbalancer.service.LoadBalancer;
import org.example.loadbalancer.strategy.LoadBalancerStrategy;
import org.example.loadbalancer.strategy.RandomStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomStrategyTest {
    private LoadBalancerStrategy strategy;
    private List<ServiceInstance> services;

    @BeforeEach
    void setup() {
        strategy = new RandomStrategy();
        services = new ArrayList<>();
        services.add(new ServiceInstance("1", "host1", 8081));
        services.add(new ServiceInstance("2", "host2", 8082));
        services.add(new ServiceInstance("3", "host3", 8083));
    }

    @Test
    void testEmptyList() {
        Optional<ServiceInstance> result = strategy.selectNext(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    @RepeatedTest(100)
    void testRandomSelectionReturnsValidService() {
        Optional<ServiceInstance> result = strategy.selectNext(services);
        assertTrue(result.isPresent());
        assertTrue(services.contains(result.get()));
    }

    @Test
    void testStatisticalDistribution() {
        // Run a large number of selections to verify statistical distribution
        final int iterations = 10000;
        Map<String, Integer> distribution = new HashMap<>();

        // Initialize counters
        for (ServiceInstance service : services) {
            distribution.put(service.getId(), 0);
        }

        // Perform selections
        for (int i = 0; i < iterations; i++) {
            ServiceInstance selected = strategy.selectNext(services).get();
            distribution.put(selected.getId(), distribution.get(selected.getId()) + 1);
        }

        // Calculate expected count per service
        double expectedCount = (double) iterations / services.size();
        double tolerance = 0.1; // Allow 10% deviation

        // Verify distribution is reasonably uniform
        for (int count : distribution.values()) {
            // Each service should be selected approximately 1/n times
            assertTrue(Math.abs(count - expectedCount) / expectedCount < tolerance,
                    "Distribution is not statistically uniform: " + distribution);
        }

        // Chi-square test for uniform distribution
        double chiSquare = services.stream()
                .mapToDouble(s -> {
                    double observed = distribution.get(s.getId());
                    double expected = expectedCount;
                    return Math.pow(observed - expected, 2) / expected;
                })
                .sum();

        // For 2 degrees of freedom (3 services - 1) and p=0.05, critical value is ~5.99
        // For 3 degrees of freedom (4 services - 1) and p=0.05, critical value is ~7.81
        double criticalValue = services.size() == 3 ? 5.99 : 7.81;

        assertTrue(chiSquare < criticalValue,
                "Chi-square test failed with value " + chiSquare + " (critical value: " + criticalValue + ")");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testConcurrentRegistration() throws InterruptedException {
        int threadCount = 9;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        LoadBalancer lb = LoadBalancerFactory.createRoundRobinLB();

        for (int i = 0; i < threadCount; i++) {
                final int id = i;
                executorService.submit(() -> {
                    try {
                        startLatch.await();

                        try {
                            ServiceInstance service = new ServiceInstance(
                                    String.valueOf(id),
                                    "host" + id,
                                    8080 + id);
                            lb.registerService(service);
                        } catch (IllegalStateException e) {
                            // Expected when registry is full
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        finishLatch.countDown();
                    }
                });
        }

        startLatch.countDown();
        finishLatch.await();
        executorService.shutdown();
        assertEquals(9, lb.getAllServices().size());
    }
}
