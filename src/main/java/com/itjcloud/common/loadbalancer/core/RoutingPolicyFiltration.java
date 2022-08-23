package com.itjcloud.common.loadbalancer.core;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.stereotype.Component;

/**
 * 微服务治理的具体实现类
 *
 * @author ygzh
 */

@Component
@RequiredArgsConstructor
public class RoutingPolicyFiltration {


    private final List<RoutingPolicy> routingPolicy;


    /**
     * 过滤
     *
     * @param serviceInstances 过滤
     * @param request          请求头
     * @return 返回
     */
    public List<ServiceInstance> filter(List<ServiceInstance> serviceInstances, Request<?> request) {
        for (RoutingPolicy policy : routingPolicy) {
            serviceInstances = policy.filtered(serviceInstances, request);
        }
        return serviceInstances;

    }


}
