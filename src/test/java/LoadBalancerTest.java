import org.example.loadbalancer.factory.LoadBalancerFactory;
import org.example.loadbalancer.model.ServiceInstance;
import org.example.loadbalancer.service.LoadBalancer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadBalancerTest {
    private LoadBalancer loadBalancer;

    @BeforeEach
    void setup() {
        loadBalancer = LoadBalancerFactory.createRoundRobinLB();
    }

    @Test
    public void testRegisterService() {
        ServiceInstance service = new ServiceInstance("1", "abc", 8080);

        loadBalancer.registerService(service);
        assertEquals(loadBalancer.getAllServices().size(), 1);
    }

    @Test
    void testRegisterDuplicateService() {
        ServiceInstance service = new ServiceInstance("1", "host1", 8081);
        assertTrue(loadBalancer.registerService(service));
        assertFalse(loadBalancer.registerService(service));
        assertEquals(1, loadBalancer.getAllServices().size());
    }

    @Test
    void testRegisterNullService() {
        assertFalse(loadBalancer.registerService(null));
    }
}
