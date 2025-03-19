package org.example.loadbalancer.model;

public class ServiceInstance {
    private String host;
    private int port;
    private String id;

    public ServiceInstance(String id, String host, int port) {
        this.id = id;
        this.port = port;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getId() {
        return id;
    }
}
