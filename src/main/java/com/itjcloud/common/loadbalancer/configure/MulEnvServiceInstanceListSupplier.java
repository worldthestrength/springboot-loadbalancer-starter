package com.itjcloud.common.loadbalancer.configure;


import com.itjcloud.common.loadbalancer.core.RoutingPolicyFiltration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.loadbalancer.core.DelegatingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;


/**
 * DelegatingServiceInstanceListSupplier 负载策略实现
 *
 * @author ygzh
 */
@Slf4j
public class MulEnvServiceInstanceListSupplier extends DelegatingServiceInstanceListSupplier {


    private final RoutingPolicyFiltration routingPolicy;

    /**
     * 设置过滤器
     *
     * @param delegate      路由
     * @param routingPolicy 过滤器链
     */
    public MulEnvServiceInstanceListSupplier(ServiceInstanceListSupplier delegate,
                                             RoutingPolicyFiltration routingPolicy) {
        super(delegate);
        this.routingPolicy = routingPolicy;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return delegate.get();
    }

    /**
     * spring webflux实现传递逻辑
     *
     * @param request
     * @return
     */
    @Override
    public Flux<List<ServiceInstance>> get(Request request) {
        return delegate.get(request)
                .map(instances -> routingPolicy.filter(instances, request));
    }


}


